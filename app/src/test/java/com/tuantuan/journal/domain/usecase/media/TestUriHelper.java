package com.tuantuan.journal.domain.usecase.media;

import android.net.Uri;

/**
 * JVM测试辅助类：提供null Uri值。
 * Java返回null给Kotlin时产生platform type (Uri!)，
 * 配合-Xno-call-assertions编译选项可绕过Kotlin运行时空检查。
 */
public class TestUriHelper {
    public static Uri nullUri() {
        return null;
    }
}