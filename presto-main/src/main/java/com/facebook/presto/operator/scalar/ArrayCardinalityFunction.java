/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.operator.scalar;

import com.facebook.presto.metadata.BoundVariables;
import com.facebook.presto.metadata.FunctionRegistry;
import com.facebook.presto.metadata.SqlScalarFunction;
import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.type.TypeManager;
import com.google.common.collect.ImmutableList;

import java.lang.invoke.MethodHandle;

import static com.facebook.presto.metadata.Signature.typeVariable;
import static com.facebook.presto.util.Reflection.methodHandle;

public final class ArrayCardinalityFunction
        extends SqlScalarFunction
{
    public static final ArrayCardinalityFunction ARRAY_CARDINALITY = new ArrayCardinalityFunction();
    private static final MethodHandle METHOD_HANDLE = methodHandle(ArrayCardinalityFunction.class, "arrayLength", Block.class);

    public ArrayCardinalityFunction()
    {
        super("cardinality", ImmutableList.of(typeVariable("E")), ImmutableList.of(), "bigint", ImmutableList.of("array(E)"));
    }

    @Override
    public boolean isHidden()
    {
        return false;
    }

    @Override
    public boolean isDeterministic()
    {
        return true;
    }

    @Override
    public String getDescription()
    {
        return "Returns the cardinality (length) of the array";
    }

    @Override
    public ScalarFunctionImplementation specialize(BoundVariables boundVariables, int arity, TypeManager typeManager, FunctionRegistry functionRegistry)
    {
        return new ScalarFunctionImplementation(false, ImmutableList.of(false), METHOD_HANDLE, isDeterministic());
    }

    public static long arrayLength(Block block)
    {
        return block.getPositionCount();
    }
}
