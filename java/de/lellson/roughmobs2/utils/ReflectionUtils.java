package de.lellson.roughmobs2.utils;

/*
 * WIP

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

 *
 */
public class ReflectionUtils {
/*
	public class MethodNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		private final String methodName;
		
		public MehodNotFoundException(String methodName, Exception failed) {
			super(failed);
			this.methodName = methodName;
		}
		
		@Override
	      public String toString()
	      {         
	         return "Cannot find Method: " + methodName;
	      }

	}
	
	   public static <E> MethodHandle findMethod(Class<? super E> clazz, String[] methodNames, Class<?>... methodTypes) throws MethodNotFoundException
	   {
	      try
	      {
	         Method method = ReflectionHelper.findMethod(clazz, null, methodNames, methodTypes);
	         return MethodHandles.lookup().unreflect(method);
	      }
	      catch(Exception excp)
	      {
	         String methodStr = Arrays.toString(methodNames) + " (" + Arrays.toString(methodTypes) + ") : <?>";
	         throw new MethodNotFoundException(methodStr, excp);
	      }
	   }
	   
	   public static String[] remapMethodNames(Class<?> clazz, String... methodNames)
	   {
	       String internalClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(clazz.getName().replace('.', '/'));
	       String[] mappedNames = new String[methodNames.length];
	       int i = 0;
	       for (String mName : methodNames)
	       {
	           mappedNames[i++] = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(internalClassName, mName, null);
	       }
	       return mappedNames;
	   }
*/
}
