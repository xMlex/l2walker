package fw.extensions.util;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * пример конструктора: new GNativeArray<String>(){}
 */

public abstract class GNativeArray<E> extends GArray<E>
{
	@SuppressWarnings("unchecked")
	public GNativeArray(int initialCapacity)
	{
		if(initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		elementData = (E[]) Array.newInstance(_getTypeArguments(GNativeArray.class, getClass()).get(0), initialCapacity);
	}

	public GNativeArray()
	{
		this(10);
	}

	/**
	 * использовать аккуратно!
	 */
	public E[] getDirectArray()
	{
		return elementData;
	}

	/**
	 * Get the underlying class for a type, or null if the type is a variable type.
	 * @param type the type
	 * @return the underlying class
	 */
	public static Class<?> _getClass(Type type)
	{
		if(type instanceof Class<?>)
			return (Class<?>) type;

		if(type instanceof ParameterizedType)
			return _getClass(((ParameterizedType) type).getRawType());

		if(!(type instanceof GenericArrayType))
			return null;

		Type componentType = ((GenericArrayType) type).getGenericComponentType();
		Class<?> componentClass = _getClass(componentType);
		if(componentClass != null)
			return Array.newInstance(componentClass, 0).getClass();
		return null;
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic base class.
	 *
	 * @param baseClass<?> the base class
	 * @param childClass<?> the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	public static <T> List<Class<?>> _getTypeArguments(Class<T> baseClass, Class<? extends T> childClass)
	{
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
		Type type = childClass;
		// start walking up the inheritance hierarchy until we hit baseClass
		while(!_getClass(type).equals(baseClass))
		{
			if(type instanceof Class<?>)
				// there is no useful information for us in raw types, so just keep going.
				type = ((Class<?>) type).getGenericSuperclass();
			else
			{
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Class<?> rawType = (Class<?>) parameterizedType.getRawType();

				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
				for(int i = 0; i < actualTypeArguments.length; i++)
					resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);

				if(!rawType.equals(baseClass))
					type = rawType.getGenericSuperclass();
			}
		}

		// finally, for each actual type argument provided to baseClass, determine (if possible)
		// the raw class for that type argument.
		Type[] actualTypeArguments = type instanceof Class<?> ? ((Class<?>) type).getTypeParameters() : ((ParameterizedType) type).getActualTypeArguments();
		List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
		// resolve types by chasing down type variables.
		for(Type baseType : actualTypeArguments)
		{
			while(resolvedTypes.containsKey(baseType))
				baseType = resolvedTypes.get(baseType);
			typeArgumentsAsClasses.add(_getClass(baseType));
		}
		return typeArgumentsAsClasses;
	}
}