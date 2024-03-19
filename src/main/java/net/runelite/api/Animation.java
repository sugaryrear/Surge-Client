/*
 * Copyright (c) 2020, Noodleeater <noodleeater4@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.api;

/**
 * Represents a frame of animation data. Each animation frame will have its own animation animation data.
 */
public interface Animation
{
	Skeleton getSkeleton();

	/**
	 * the number of transformations the animation frame has.
	 */
	int getTransformCount();

	/**
	 * this variable name is incorrect. it is an array of bone ids. not transform types.
	 * this array has a one to one relationship with the arrays of translator values in the animation frame(TranslatorX getTranslatorY TranslatorZ).
	 * it is the array of bone ids which point each translator value to the bone it should be transforming.
	 */
	int[] getTransformTypes();

	/**
	 * these are x, y, and z values, which tell the transform function how much to transform the each bone.
	 */
	int[] getTranslatorX();

	int[] getTranslatorY();

	int[] getTranslatorZ();

	/**
	 * whether this Animation frame has any alpha/transparency animation.
	 */
	boolean isShowing();
}
