/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.gradle.dist.service;

import com.google.common.collect.Maps;
import com.palantir.gradle.dist.BaseDistributionExtension;
import com.palantir.gradle.dist.ProductType;
import com.palantir.gradle.dist.service.gc.GcProfile;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.util.ConfigureUtil;

public class JavaServiceDistributionExtension extends BaseDistributionExtension {

    private final Property<String> mainClass;
    private final Property<String> javaHome;
    private final Property<Boolean> addJava8GcLogging;
    private final Property<Boolean> enableManifestClasspath;
    private final Property<GcProfile> gc;
    private final ListProperty<String> args;
    private final ListProperty<String> checkArgs;
    private final ListProperty<String> defaultJvmOpts;
    private final ListProperty<String> excludeFromVar;
    // TODO(forozco): Use MapProperty once our minimum supported version is 5.1
    private Map<String, String> env;

    private final ObjectFactory objectFactory;

    @Inject
    public JavaServiceDistributionExtension(Project project) {
        super(project);

        objectFactory = project.getObjects();
        mainClass = objectFactory.property(String.class);
        javaHome = objectFactory.property(String.class);
        addJava8GcLogging = objectFactory.property(Boolean.class);
        addJava8GcLogging.set(false);
        enableManifestClasspath = objectFactory.property(Boolean.class);
        enableManifestClasspath.set(false);
        gc = objectFactory.property(GcProfile.class);
        gc.set(new GcProfile.Throughput());
        args = objectFactory.listProperty(String.class);
        checkArgs = objectFactory.listProperty(String.class);
        defaultJvmOpts = objectFactory.listProperty(String.class);
        excludeFromVar = objectFactory.listProperty(String.class);
        excludeFromVar.addAll("log", "run");
        env = Maps.newHashMap();

        setProductType(ProductType.SERVICE_V1);
    }

    public final Provider<String> getMainClass() {
        return mainClass;
    }

    public final void mainClass(String newMainClass) {
        this.mainClass.set(newMainClass);
    }

    public final Provider<String> getJavaHome() {
        return javaHome;
    }

    public final void javaHome(String newJavaHome) {
        this.javaHome.set(newJavaHome);
    }

    public final Provider<Boolean> getAddJava8GcLogging() {
        return addJava8GcLogging;
    }

    public final void setAddJava8GcLogging(boolean addJava8GcLogging) {
        this.addJava8GcLogging.set(addJava8GcLogging);
    }

    public final void addJava8GcLogging(boolean newAddJava8GcLogging) {
        this.addJava8GcLogging.set(newAddJava8GcLogging);
    }

    public final Provider<Boolean> getEnableManifestClasspath() {
        return enableManifestClasspath;
    }

    public final void enableManifestClasspath(boolean newEnableManifestClasspath) {
        this.enableManifestClasspath.set(newEnableManifestClasspath);
    }

    public final Provider<List<String>> getArgs() {
        return args;
    }

    public final void args(String... newArgs) {
        this.args.addAll(newArgs);
    }

    public final void args(Provider<Iterable<String>> newArgs) {
        this.args.addAll(newArgs);
    }

    public final void setArgs(Iterable<String> args) {
        this.args.set(args);
    }

    public final Provider<List<String>> getCheckArgs() {
        return checkArgs;
    }

    public final void checkArgs(String... newCheckArgs) {
        this.checkArgs.addAll(newCheckArgs);
    }

    public final void checkArgs(Provider<Iterable<String>> newCheckArgs) {
        this.checkArgs.addAll(newCheckArgs);
    }

    public final void setCheckArgs(Iterable<String> checkArgs) {
        this.checkArgs.set(checkArgs);
    }

    public final Provider<List<String>> getDefaultJvmOpts() {
        return defaultJvmOpts;
    }

    public final void defaultJvmOpts(String... opts) {
        this.defaultJvmOpts.addAll(opts);
    }

    public final void defaultJvmOpts(Provider<Iterable<String>> opts) {
        this.defaultJvmOpts.addAll(opts);
    }

    public final void setDefaultJvmOpts(Iterable<String> defaultJvmOpts) {
        this.defaultJvmOpts.set(defaultJvmOpts);
    }

    public final Provider<List<String>> getExcludeFromVar() {
        return excludeFromVar;
    }

    public final void excludeFromVar(String... fromVar) {
        this.excludeFromVar.addAll(fromVar);
    }

    public final void excludeFromVar(Provider<Iterable<String>> fromVar) {
        this.excludeFromVar.addAll(fromVar);
    }

    public final void setExcludeFromVar(Iterable<String> excludeFromVar) {
        this.excludeFromVar.set(excludeFromVar);
    }

    public final Map<String, String> getEnv() {
        return env;
    }

    public final void env(Map<String, String> newEnv) {
        this.env.putAll(newEnv);
    }

    public final void setEnv(Map<String, String> env) {
        this.env = env;
    }

    public final Provider<GcProfile> getGc() {
        return gc;
    }

    public final void gc(String type, @Nullable @DelegatesTo(GcProfile.class) Closure configuration) {
        GcProfile newGc = objectFactory.newInstance(GcProfile.PROFILE_NAMES.get(type));
        if (configuration != null) {
            ConfigureUtil.configure(configuration, newGc);
        }
        gc.set(newGc);
    }

    public final void gc(String type) {
        gc(type, null);
    }

    public final <T extends GcProfile> void gc(Class<T> type, @Nullable Action<T> action) {
        // separate variable since 'gc' has type GcProfile and we need to give the action a 'T'
        T instance = objectFactory.newInstance(type);
        gc.set(instance);
        if (action != null) {
            action.execute(instance);
        }
    }

    public final <T extends GcProfile> void gc(Class<T> type) {
        gc(type, null);
    }
}
