package com.youdrop.youdrop.api;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class DeserializeStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            //f.getDeclaringClass() == Student.class
            return ( f.getName().contains("Id") );
        }

    }