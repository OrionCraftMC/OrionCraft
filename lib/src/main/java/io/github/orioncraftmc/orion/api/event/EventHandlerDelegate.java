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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class EventHandlerDelegate implements Opcodes {
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

	public static EventHandlerDelegate generateStaticMethodHandler(Method method, String className) {
		ClassWriter classWriter = new ClassWriter(0);
		FieldVisitor fieldVisitor;
		MethodVisitor methodVisitor;
		AnnotationVisitor annotationVisitor0;

		classWriter.visit(
				V1_8,
				ACC_PUBLIC | ACC_SUPER,
				className,
				null,
				"io/github/orioncraftmc/orion/api/event/EventHandlerDelegate",
				null
		);
		classWriter.visitSource(className + ".java", null);

		{
			methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			methodVisitor.visitCode();
			Label label0 = new Label();
			methodVisitor.visitLabel(label0);
			methodVisitor.visitLineNumber(5, label0);
			methodVisitor.visitVarInsn(ALOAD, 0);
			methodVisitor.visitMethodInsn(INVOKESPECIAL, "io/github/orioncraftmc/orion/api/event/EventHandlerDelegate", "<init>", "()V", false);
			methodVisitor.visitInsn(RETURN);
			Label label1 = new Label();
			methodVisitor.visitLabel(label1);
			methodVisitor.visitLocalVariable("this", "Lio/github/orioncraftmc/orion/api/event/TestThing;", null, label0, label1, 0);
			methodVisitor.visitMaxs(1, 1);
			methodVisitor.visitEnd();
		}

		{
			methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "call", "(Lio/github/orioncraftmc/orion/api/event/Event;)V", null, null);
			methodVisitor.visitCode();
			Label label0 = new Label();
			methodVisitor.visitVarInsn(ALOAD, 1);
			methodVisitor.visitTypeInsn(CHECKCAST, method.getParameterTypes()[0].getName().replace('.', '/'));
			methodVisitor.visitMethodInsn(
					INVOKESTATIC,
					method.getDeclaringClass().getName().replace('.', '/'),
					method.getName(),
					"(L" + method.getParameterTypes()[0].getName().replace('.', '/') + ";)V", false);
			Label label1 = new Label();
			methodVisitor.visitLabel(label1);
			methodVisitor.visitLineNumber(9, label1);
			methodVisitor.visitInsn(RETURN);
			Label label2 = new Label();
			methodVisitor.visitLabel(label2);
			methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label2, 0);
			methodVisitor.visitLocalVariable("event", "Lio/github/orioncraftmc/orion/api/event/Event;", null, label0, label2, 1);
			methodVisitor.visitMaxs(1, 2);
			methodVisitor.visitEnd();
		}

		classWriter.visitEnd();

		byte[] arr = classWriter.toByteArray();
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
			return (EventHandlerDelegate) clazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
