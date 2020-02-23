package com.youdrop.youdrop.api;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class SerializeStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            //f.getDeclaringClass() == Student.class
            String name = f.getName();
            String type = f.getDeclaredType().toString();
            return ( name.equals("user") || name.equals("category") || name.equals("file") || name.equals("receiver") || name.equals("avatar") );
        }

    }