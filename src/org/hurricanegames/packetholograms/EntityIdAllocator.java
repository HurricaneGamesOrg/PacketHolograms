package org.hurricanegames.packetholograms;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.hurricanegames.commandlib.utils.ReflectionUtils;

import com.comphenix.protocol.utility.MinecraftReflection;

public class EntityIdAllocator {

	private final AtomicInteger entityCounter;

	public EntityIdAllocator() {
		this.entityCounter = ReflectionUtils.getField(findField(), null);
	}

	protected static Field findField() {
		Class<?> entityClass = MinecraftReflection.getEntityClass();
		Predicate<Field> isEntityCountField = field -> Modifier.isStatic(field.getModifiers()) && AtomicInteger.class.isAssignableFrom(field.getType());

		try {
			Field field = ReflectionUtils.setAccessible(entityClass.getDeclaredField("entityCount"));
			if (isEntityCountField.test(field)) {
				return field;
			}
		} catch (NoSuchFieldException e) {
		}

		for (Field field : entityClass.getDeclaredFields()) {
			if (isEntityCountField.test(field)) {
				return field;
			}
		}

		throw new IllegalArgumentException("Unable to find an entity id allocator field in class " + entityClass);
	}

	public int allocate() {
		return entityCounter.incrementAndGet();
	}

}
