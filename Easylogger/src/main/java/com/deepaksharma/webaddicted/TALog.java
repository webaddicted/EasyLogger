/*
 * Copyright 2015 Elvis Hew
 *
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

package com.deepaksharma.webaddicted;

import android.content.Context;

import com.deepaksharma.webaddicted.formatter.border.BorderFormatter;
import com.deepaksharma.webaddicted.formatter.message.json.JsonFormatter;
import com.deepaksharma.webaddicted.formatter.message.object.ObjectFormatter;
import com.deepaksharma.webaddicted.formatter.message.throwable.ThrowableFormatter;
import com.deepaksharma.webaddicted.formatter.message.xml.XmlFormatter;
import com.deepaksharma.webaddicted.formatter.stacktrace.StackTraceFormatter;
import com.deepaksharma.webaddicted.formatter.thread.ThreadFormatter;
import com.deepaksharma.webaddicted.interceptor.Interceptor;
import com.deepaksharma.webaddicted.internal.DefaultsFactory;
import com.deepaksharma.webaddicted.internal.Platform;
import com.deepaksharma.webaddicted.internal.util.StackTraceUtil;
import com.deepaksharma.webaddicted.printer.Printer;
import com.deepaksharma.webaddicted.printer.PrinterSet;


public class TALog {//implements PermissionListener{

    /**
     * Global logger for all direct logging via {@link TALog}.
     */
    private static Logger sLogger;

    /**
     * Global log configuration.
     */
    static LogConfiguration sLogConfiguration;

    /**
     * Global log printer.
     */
    static Printer sPrinter;

    static boolean sIsInitialized;

    private static boolean isBorderEnable = false;

    private static boolean isThreadInfo = false;

    private static int stackTraceDept = 0;

    public static Context mContext;

    /**
     * Prevent instance.
     */
    public TALog() {
    }


    /**
     * Initialize log system, should be called only once.
     *
     * @param logLevel the log level, logs with a lower level than which would not be printed
     */
    public static void init(Context context,int logLevel) {
        mContext=context;
        init(new LogConfiguration.Builder().logLevel(logLevel).build(),
                DefaultsFactory.createPrinter());
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logLevel         the log level, logs with a lower level than which would not be printed
     * @param logConfiguration the log configuration
     * @deprecated the log level is part of log configuration now, use {@link #init(LogConfiguration)}
     * instead, since 1.3.0
     */
    @Deprecated
    public static void init(int logLevel, LogConfiguration logConfiguration) {
        init(new LogConfiguration.Builder(logConfiguration).logLevel(logLevel).build());
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logConfiguration the log configuration
     */
    public static void init(LogConfiguration logConfiguration) {
        init(logConfiguration, DefaultsFactory.createPrinter());
    }


    /**
     * Initialize log system, should be called only once.
     *
     * @param logLevel         the log level, logs with a lower level than which would not be printed
     * @param logConfiguration the log configuration
     * @param printers         the printers, each log would be printed by all of the printers
     * @deprecated the log level is part of log configuration now,
     * use {@link #init(LogConfiguration, Printer...)} instead, since 1.3.0
     */
    @Deprecated
    public static void init(int logLevel, LogConfiguration logConfiguration, Printer... printers) {
        init(new LogConfiguration.Builder(logConfiguration).logLevel(logLevel).build(), printers);
    }

    /**
     * Initialize log system, should be called only once.
     *
     * @param logConfiguration the log configuration
     * @param printers         the printers, each log would be printed by all of the printers
     */
    public static void init(LogConfiguration logConfiguration, Printer... printers) {
        if (sIsInitialized) {
            Platform.get().warn("TALog is already initialized, do not initialize again");
        }
        sIsInitialized = true;

        if (logConfiguration == null) {
            throw new IllegalArgumentException("Please specify a LogConfiguration");
        }
        sLogConfiguration = logConfiguration;

        sPrinter = new PrinterSet(printers);

        sLogger = new Logger(sLogConfiguration, sPrinter);
    }

    /**
     * Throw an IllegalStateException if not initialized.
     */
    static void assertInitialization() {
        if (!sIsInitialized) {
            throw new IllegalStateException("Do you forget to initialize TALog?");
        }
    }

    /**
     * Start to customize a {@link Logger} and set the log level.
     *
     * @param logLevel the log level to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder logLevel(int logLevel) {
        return new Logger.Builder().logLevel(logLevel);
    }

    /**
     * Start to customize a {@link Logger} and set the tag.
     *
     * @param tag the tag to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    private static Logger.Builder tag(String tag) {
        return new Logger.Builder().tag(tag);
    }

    /**
     * Start to customize a {@link Logger} and enable thread info.
     *
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static void enableThreadInfo(boolean threadInfo) {
        isThreadInfo = threadInfo;
//        return new Logger.Builder().t();
    }


    public static void setStacktraceInfo(int dept) {
        stackTraceDept = dept;
    }

    /**
     * Start to customize a {@link Logger} and disable thread info.
     *
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    private static Logger.Builder nt() {
        return new Logger.Builder().nt();
    }

    /**
     * Start to customize a {@link Logger} and enable stack trace.
     *
     * @param depth the number of stack trace elements we should log, 0 if no limitation
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    private static Logger.Builder st(int depth) {
        return new Logger.Builder().st(depth);
    }

    /**
     * Start to customize a {@link Logger} and enable stack trace.
     *
     * @param stackTraceOrigin the origin of stack trace elements from which we should NOT log,
     *                         it can be a package name like "com.elvishew.xlog", a class name
     *                         like "com.yourdomain.logWrapper", or something else between
     *                         package name and class name, like "com.yourdomain.".
     *                         It is mostly used when you are using a logger wrapper
     * @param depth            the number of stack trace elements we should log, 0 if no limitation
     * @return the {@link Logger.Builder} to build the {@link Logger}
     * @since 1.4.0
     */
    private static Logger.Builder st(String stackTraceOrigin, int depth) {
        return new Logger.Builder().st(stackTraceOrigin, depth);
    }

    /**
     * Start to customize a {@link Logger} and disable stack trace.
     *
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    private static Logger.Builder nst() {
        return new Logger.Builder().nst();
    }

    /**
     * Start to customize a {@link Logger} and enable border.
     *
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static void enableBorder(boolean borderSet) {
        isBorderEnable = borderSet;
//    return new Logger.Builder().b();
    }

    /**
     * Start to customize a {@link Logger} and disable border.
     *
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    private static Logger.Builder nb() {
        return new Logger.Builder().nb();
    }

    /**
     * Start to customize a {@link Logger} and set the {@link JsonFormatter}.
     *
     * @param jsonFormatter the {@link JsonFormatter} to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder jsonFormatter(JsonFormatter jsonFormatter) {
        return new Logger.Builder().jsonFormatter(jsonFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link XmlFormatter}.
     *
     * @param xmlFormatter the {@link XmlFormatter} to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder xmlFormatter(XmlFormatter xmlFormatter) {
        return new Logger.Builder().xmlFormatter(xmlFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link ThrowableFormatter}.
     *
     * @param throwableFormatter the {@link ThrowableFormatter} to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
        return new Logger.Builder().throwableFormatter(throwableFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link ThreadFormatter}.
     *
     * @param threadFormatter the {@link ThreadFormatter} to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder threadFormatter(ThreadFormatter threadFormatter) {
        return new Logger.Builder().threadFormatter(threadFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link StackTraceFormatter}.
     *
     * @param stackTraceFormatter the {@link StackTraceFormatter} to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder stackTraceFormatter(StackTraceFormatter stackTraceFormatter) {
        return new Logger.Builder().stackTraceFormatter(stackTraceFormatter);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link BorderFormatter}.
     *
     * @param borderFormatter the {@link BorderFormatter} to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder borderFormatter(BorderFormatter borderFormatter) {
        return new Logger.Builder().borderFormatter(borderFormatter);
    }

    /**
     * Start to customize a {@link Logger} and add an object formatter for specific class of object.
     *
     * @param objectClass     the class of object
     * @param objectFormatter the object formatter to add
     * @param <T>             the type of object
     * @return the {@link Logger.Builder} to build the {@link Logger}
     * @since 1.1.0
     */
    public static <T> Logger.Builder addObjectFormatter(Class<T> objectClass,
                                                        ObjectFormatter<? super T> objectFormatter) {
        return new Logger.Builder().addObjectFormatter(objectClass, objectFormatter);
    }

    /**
     * Start to customize a {@link Logger} and add an interceptor.
     *
     * @param interceptor the interceptor to add
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder addInterceptor(Interceptor interceptor) {
        return new Logger.Builder().addInterceptor(interceptor);
    }

    /**
     * Start to customize a {@link Logger} and set the {@link Printer} array.
     *
     * @param printers the {@link Printer} array to customize
     * @return the {@link Logger.Builder} to build the {@link Logger}
     */
    public static Logger.Builder printers(Printer... printers) {
        return new Logger.Builder().printers(printers);
    }

    /**
     * Log an object with level {@link LogLevel#VERBOSE}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void verbose(Object object) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.v(object);
        } else {
            sLogger.v(object);
        }
    }

    /**
     * Log an array with level {@link LogLevel#VERBOSE}.
     *
     * @param array the array to log
     */
    public static void verbose(Object[] array) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.v(array);
        } else {
            sLogger.v(array);
        }
    }

    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void verbose(String format, Object... args) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.v(format, args);
        } else {
            sLogger.v(format, args);
        }
    }

    /**
     * Log a message with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     */
    public static void verbose(String msg) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.v(msg);
        } else {
            sLogger.v(msg);
        }
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#VERBOSE}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void verbose(String msg, Throwable tr) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.v(msg, tr);
        } else {
            sLogger.v(msg, tr);
        }
    }

    /**
     * Log an object with level {@link LogLevel#DEBUG}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void debug(Object object) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.d(object);
        } else {
            sLogger.d(object);
        }
    }

    /**
     * Log an array with level {@link LogLevel#DEBUG}.
     *
     * @param array the array to log
     */
    public static void debug(Object[] array) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.d(array);
        } else {
            sLogger.d(array);
        }
    }

    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void debug(String format, Object... args) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.d(format, args);
        } else {
            sLogger.d(format, args);
        }
    }

    /**
     * Log a message with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     */
    public static void debug(String msg) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.d(msg);
        } else {
            sLogger.d(msg);
        }


    }

    private static Logger getLogger() {
        Logger.Builder builder = new Logger.Builder();
        if (isBorderEnable) {
            builder.b();
        }
        if (isThreadInfo) {
            builder.t();
        }
        if (stackTraceDept != 0) {
            builder.st(stackTraceDept);
        }
        return builder.build();
    }

    private static Logger.Builder getEnableLogger() {
        return new Logger.Builder();
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#DEBUG}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void debug(String msg, Throwable tr) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.d(msg, tr);
        } else {
            sLogger.d(msg, tr);
        }
    }

    /**
     * Log an object with level {@link LogLevel#INFO}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void info(Object object) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.i(object);
        } else {
            sLogger.i(object);
        }
    }

    /**
     * Log an array with level {@link LogLevel#INFO}.
     *
     * @param array the array to log
     */
    public static void info(Object[] array) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.i(array);
        } else {
            sLogger.i(array);
        }
    }

    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void info(String format, Object... args) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.i(format, args);
        } else {
            sLogger.i(format, args);
        }
    }

    /**
     * Log a message with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     */
    public static void info(String msg) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.i(msg);
        } else {
            sLogger.i(msg);
        }
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#INFO}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void info(String msg, Throwable tr) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.i(msg, tr);
        } else {
            sLogger.i(msg, tr);
        }
    }

    /**
     * Log an object with level {@link LogLevel#WARN}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void warn(Object object) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.w(object);
        } else {
            sLogger.w(object);
        }
    }

    /**
     * Log an array with level {@link LogLevel#WARN}.
     *
     * @param object the array to log
     */
    public static void warn(Object[] object) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.w(object);
        } else {
            sLogger.w(object);
        }
    }

    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void warn(String format, Object... args) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.w(format, args);
        } else {
            sLogger.w(format, args);
        }
    }

    /**
     * Log a message with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     */
    public static void warn(String msg) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.w(msg);
        } else {
            sLogger.w(msg);
        }
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#WARN}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void warn(String msg, Throwable tr) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.w(msg, tr);
        } else {
            sLogger.w(msg, tr);
        }

    }

    /**
     * Log an object with level {@link LogLevel#ERROR}.
     *
     * @param object the object to log
     * @see LogConfiguration.Builder#addObjectFormatter(Class, ObjectFormatter)
     * @since 1.1.0
     */
    public static void error(Object object) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.e(object);
        } else {
            sLogger.e(object);
        }

    }

    /**
     * Log an array with level {@link LogLevel#ERROR}.
     *
     * @param array the array to log
     */
    public static void error(Object[] array) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.e(array);
        } else {
            sLogger.e(array);
        }
    }

    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param format the format of the message to log
     * @param args   the arguments of the message to log
     */
    public static void error(String format, Object... args) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.e(format, args);
        } else {
            sLogger.e(format, args);
        }

    }

    /**
     * Log a message with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     */
    public static void error(String msg) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.e(msg);
        } else {
            sLogger.e(msg);
        }
    }

    /**
     * Log a message and a throwable with level {@link LogLevel#ERROR}.
     *
     * @param msg the message to log
     * @param tr  the throwable to be log
     */
    public static void error(String msg, Throwable tr) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.e(msg);
        } else {
            sLogger.e(msg);
        }
    }


    /**
     * Log a message and a throwable with specific log level.
     *
     * @param logLevel the specific log level
     * @param msg      the message to log
     * @param tr       the throwable to be log
     * @since 1.4.0
     */
    /*public static void log(int logLevel, String msg, Throwable tr) {
        assertInitialization();
        sLogger.log(logLevel, msg, tr);
    }*/

    /**
     * Log a JSON string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param json the JSON string to log
     */
    public static void json(String json) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.json(json);
        } else {
            sLogger.json(json);
        }
    }

    /**
     * Log a XML string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param xml the XML string to log
     */
    public static void xml(String xml) {
        assertInitialization();
        if (isBorderEnable || isThreadInfo || stackTraceDept > 0) {
            Logger logger = getLogger();
            logger.xml(xml);
        } else {
            sLogger.xml(xml);
        }
    }


//    @Override
//    public void onAccepted() {
//
//    }
//
//    @Override
//    public void onDenied() {
//
//    }

    /**
     * Compatible class with {@link android.util.Log}.
     *
     * @deprecated please use {@link TALog} instead
     */
    public static class Log {

        /**
         * @deprecated compatible with {@link android.util.Log#v(String, String)}
         */
        public static void v(String tag, String msg) {
            tag(tag).build().v(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#v(String, String, Throwable)}
         */
        public static void v(String tag, String msg, Throwable tr) {
            tag(tag).build().v(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#d(String, String)}
         */
        public static void d(String tag, String msg) {
            tag(tag).build().d(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#d(String, String, Throwable)}
         */
        public static void d(String tag, String msg, Throwable tr) {
            tag(tag).build().d(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#i(String, String)}
         */
        public static void i(String tag, String msg) {
            tag(tag).build().i(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#i(String, String, Throwable)}
         */
        public static void i(String tag, String msg, Throwable tr) {
            tag(tag).build().i(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, String)}
         */
        public static void w(String tag, String msg) {
            tag(tag).build().w(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, String, Throwable)}
         */
        public static void w(String tag, String msg, Throwable tr) {
            tag(tag).build().w(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#w(String, Throwable)}
         */
        public static void w(String tag, Throwable tr) {
            tag(tag).build().w("", tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#e(String, String)}
         */
        public static void e(String tag, String msg) {
            tag(tag).build().e(msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#e(String, String, Throwable)}
         */
        public static void e(String tag, String msg, Throwable tr) {
            tag(tag).build().e(msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, String)}
         */
        public static void wtf(String tag, String msg) {
            e(tag, msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, Throwable)}
         */
        public static void wtf(String tag, Throwable tr) {
            wtf(tag, "", tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#wtf(String, String, Throwable)}
         */
        public static void wtf(String tag, String msg, Throwable tr) {
            e(tag, msg, tr);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#println(int, String, String)}
         */
        public static void println(int logLevel, String tag, String msg) {
            tag(tag).build().println(logLevel, msg);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#isLoggable(String, int)}
         */
        public static boolean isLoggable(String tag, int level) {
            return sLogConfiguration.isLoggable(level);
        }

        /**
         * @deprecated compatible with {@link android.util.Log#getStackTraceString(Throwable)}
         */
        public static String getStackTraceString(Throwable tr) {
            return StackTraceUtil.getStackTraceString(tr);
        }
    }
}
