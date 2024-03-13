
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft;

import org.merrymike.soft.handler.AppRunHandler;

public class Main {
    private static final AppRunHandler appRunHandler = new AppRunHandler();

    public static void main(String[] args) {
        appRunHandler.runApp(args);
    }
}