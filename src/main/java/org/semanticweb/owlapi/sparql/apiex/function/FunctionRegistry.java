/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, The University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owlapi.sparql.apiex.function;

import org.semanticweb.owlapi.sparql.apiex.function.types.TypeFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2012
 */
public class FunctionRegistry {

    private static FunctionRegistry instance = new FunctionRegistry();
    
    private Map<String, FunctionCall> nameMap = new HashMap<String, FunctionCall>();

    private FunctionRegistry() {
        add(new Strlen());
        add(new RegEx());
    }
    
    public static  FunctionRegistry getRegistry() {
        return instance;
    }

    public void add(FunctionCall functionCall) {
        Class<?> cls = functionCall.getClass();
        BuiltInFunction annotation = cls.getAnnotation(BuiltInFunction.class);
        if(annotation == null) {
            throw new RuntimeException("Specified function call is not annotated as a function");
        }
        String functionName = functionCall.getName();
        nameMap.put(functionName.toLowerCase(), functionCall);
    }
    
    public boolean isRegistered(String functionName) {
        return nameMap.containsKey(functionName.toLowerCase());
    }
    
    public List<Integer> getArgCounts(String functionName) {
        FunctionCall functionCall = nameMap.get(functionName.toLowerCase());
        Class<?> functionCallClass = functionCall.getClass();
        List<Integer> result = new ArrayList<Integer>();
        for(Method method : functionCallClass.getMethods()) {
            if(method.getAnnotation(BuiltInFunctionCall.class) != null) {
                Class<?> [] params = method.getParameterTypes();
                result.add(params.length);
            }
        }
        Collections.sort(result);
        return result;
    }


    public List<List<Class<?>>> getArgTypes(String functionName) {
        FunctionCall functionCall = nameMap.get(functionName.toLowerCase());
        if(functionCall == null) {
            return Collections.emptyList();
        }
        Class<?> functionCallClass = functionCall.getClass();
        List<List<Class<?>>> result = new ArrayList<List<Class<?>>>();
        for(Method method : functionCallClass.getMethods()) {
            if(method.getAnnotation(BuiltInFunctionCall.class) != null) {
                Class<?> [] params = method.getParameterTypes();
                List<Class<?>> paramsList = Arrays.asList(params);
                result.add(paramsList);
            }
        }
        return result;
    }
    
    public List<Class<?>> getArgTypesAt(String functionName, int argIndex) {
        FunctionCall functionCall = nameMap.get(functionName.toLowerCase());
        Class<?> functionCallClass = functionCall.getClass();
        List<Class<?>> result = new ArrayList<Class<?>>();
        for(Method method : functionCallClass.getMethods()) {
            if(method.getAnnotation(BuiltInFunctionCall.class) != null) {
                Class<?> [] params = method.getParameterTypes();
                if(argIndex < params.length) {
                    result.add(params[argIndex]);
                }
            }
        }
        return result;
    }
    
    public Object callFunction(String functionName, Object ... arguments) {
        try {
            FunctionCall functionCall = nameMap.get(functionName.toLowerCase());
            if(functionCall == null) {
                throw new RuntimeException("Undefined function");
            }
            Class<?> functionCallClass = functionCall.getClass();
            for(Method method : functionCallClass.getMethods()) {
                if(method.getAnnotation(BuiltInFunctionCall.class) != null) {
                    Class [] params = method.getParameterTypes();
                    if(params.length == arguments.length) {
                        if(isAssignable(params, arguments)) {
                           // Call
                            return method.invoke(functionCall, arguments);
                        }
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Illegal arguments");
        
    }

    private boolean isAssignable(Class[] params, Object[] arguments) {
        for(int argIndex = 0; argIndex < params.length; argIndex++) {
            if(!params[argIndex].isAssignableFrom(arguments[argIndex].getClass())) {
                return false;
            }
        }
        return true;
    }



    public static void main(String[] args) {
        FunctionRegistry registry = new FunctionRegistry();
        registry.add(new Strlen());
        Object value = null;
        for (int i = 0; i < 100; i++) {
            long t0 = System.currentTimeMillis();
            value = registry.callFunction("STRLEN", TypeFactory.getStringLiteral("My string"));
            long t1 = System.currentTimeMillis();
            System.out.println("Time: " + (t1 - t0));
        }
        System.out.println(value);

    }
}
