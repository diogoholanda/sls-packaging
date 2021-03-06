/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.gradle.dist.service.tasks;

import com.palantir.gradle.dist.service.JavaServiceDistributionPlugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public class CopyYourkitLicenseTask extends DefaultTask {
    public CopyYourkitLicenseTask() {
        setGroup(JavaServiceDistributionPlugin.GROUP_NAME);
        setDescription("Copies YourKit license");
    }

    @OutputFile
    public final File getOutputFile() {
        return new File(getProject().getBuildDir(), "libs/linux-x86-64/yourkit-license-redist.txt");
    }

    @TaskAction
    public final void copyYourkitLicense() throws IOException {
        InputStream src = JavaServiceDistributionPlugin.class.getResourceAsStream("/yourkit-license-redist.txt");
        Path dest = getOutputFile().toPath();
        dest.getParent().toFile().mkdirs();
        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }

}
