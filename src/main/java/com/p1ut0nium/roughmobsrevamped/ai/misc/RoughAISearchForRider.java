package com.p1ut0nium.roughmobsrevamped.ai.misc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.p1ut0nium.roughmobsrevamped.util.Constants;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.AbstractHorse;

public class RoughAISearchForRider extends EntityAIBase {

	public static final String MOUNT_SEARCHER = Constants.unique("mountSearcher");
	public static final int IS_SEARCHER = 2;
	public static final int NO_SEARCHER = 1;

	protected EntityLiving entity;
	protected List<Class<? extends Entity>> possibleRiders;
	protected int range;
	protected int chance;

	protected EntityLiving mountSearcher;

	protected static Cache<UUID, Integer> searcherCache =
		CacheBuilder
			.newBuilder()
			.maximumSize(128)
			.build();

	public RoughAISearchForRider(EntityLiving entity, List<Class<? extends Entity>> possibleRiders, int range,
								 int chance) {
		this.entity = entity;
		this.possibleRiders = possibleRiders;
		this.range = range;
		this.chance = chance;
	}

	@Override
	public boolean shouldExecute() {

		if (entity.isDead || entity.isBeingRidden()) {
			mountSearcher = null;
			return false;
		}

		List<EntityLiving> entities = entity.world
			.getEntitiesWithinAABB(EntityLiving.class, entity.getEntityBoundingBox().grow(range));

		for (EntityLiving searcher : entities) {
			if (!searcher.isDead && this.entity != searcher && isPossibleRider(searcher)) {
				UUID uuid = searcher.getUniqueID();
				Integer searchState = searcherCache.getIfPresent(uuid);

				if (
					searchState == null
						&& searcher.getEntityData().getInteger(MOUNT_SEARCHER) == 0
				) {
					searchState =
						searcher.world.rand.nextInt(chance) == 0 || searcher.isRiding() ? IS_SEARCHER : NO_SEARCHER;
					searcherCache.put(uuid, searchState);
					searcher.getEntityData().setInteger(MOUNT_SEARCHER, searchState);
				}

				if (searchState != null && searchState == IS_SEARCHER && !searcher.isRiding()) {
					mountSearcher = searcher;
					return true;
				}
			}
		}

		mountSearcher = null;
		return false;
	}

	@Override
	public void updateTask() {

		this.mountSearcher.getNavigator().setPath(mountSearcher.getNavigator().getPathToEntityLiving(entity), 1);

		if (this.entity.getDistanceSq(this.mountSearcher) <= 2 && this.mountSearcher != this.entity) {
			if (this.entity instanceof AbstractHorse) {
				AbstractHorse horse = (AbstractHorse) this.entity;
				horse.hurtResistantTime = 60;
				horse.enablePersistence();
				horse.setHorseTamed(true);
			}

			this.mountSearcher.startRiding(this.entity);
			this.mountSearcher.getNavigator().clearPath();
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return (!(this.mountSearcher instanceof EntityCreature) || ((EntityCreature) this.mountSearcher)
			.isWithinHomeDistanceFromPosition(entity.getPosition())) && this.mountSearcher.getAttackTarget() == null
			&& !this.entity.isDead && !this.entity.isBeingRidden() && !this.mountSearcher.isRiding()
			&& this.mountSearcher != this.entity;
	}

	@Override
	public void resetTask() {
		this.mountSearcher.getNavigator().clearPath();
		this.mountSearcher = null;
	}

	private boolean isPossibleRider(EntityLivingBase entity) {

		for (Class<? extends Entity> clazz : possibleRiders) { if (clazz.isInstance(entity)) { return true; } }

		return false;
	}
}
