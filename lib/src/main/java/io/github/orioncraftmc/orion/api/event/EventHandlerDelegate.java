/*
 * Copyright (C) 2021 OrionCraftMC
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.github.orioncraftmc.orion.api.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public abstract class EventHandlerDelegate {
	private static final MethodHandle DEFINE_CLASS;

	static {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		Method m;
		try {
			m = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		m.setAccessible(true);
		try {
			DEFINE_CLASS = lookup.unreflect(m);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract void call(Event event);

	public static Object compile(Method method, String className) {
		ClassWriter cw = new ClassWriter(0);
		cw.visit(52,
				Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER,
				className,
				null,
				"java/lang/Object",
				null
				);
		cw.visitSource(className + ".java", null);

		{ // Constructor
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
					"java/lang/Object",
					"<init>",
					"()V",
					false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		{ // Overriding method

		}

		byte[] arr = cw.toByteArray();
		ClassLoader cl = EventHandlerDelegate.class.getClassLoader();
		try {
			DEFINE_CLASS.invokeWithArguments(cl, className, arr, 0, arr.length);
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}

		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			return clazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
