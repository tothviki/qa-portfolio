package com.portfolio.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations

import javax.inject.Inject

abstract class PythonVenvTask extends DefaultTask {
    @Input
    abstract Property<String> getBootstrapPython()

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    abstract ConfigurableFileCollection getSourceFiles()

    @InputFile
    abstract RegularFileProperty getPyprojectFile()

    @InputFile
    abstract RegularFileProperty getPytestIniFile()

    @InputFile
    abstract RegularFileProperty getRequirementsFile()

    @InputFile
    abstract RegularFileProperty getDevRequirementsFile()

    @Internal
    abstract DirectoryProperty getModuleDirectory()

    @Inject
    abstract ExecOperations getExecOperations()

    @Internal
    File getVenvPython() {
        return new File(moduleDirectory.get().asFile, '.venv/bin/python')
    }

    void ensureVenv() {
        File moduleDir = moduleDirectory.get().asFile
        File venvPython = getVenvPython()
        if (!venvPython.exists()) {
            execOperations.exec {
                workingDir = moduleDir
                commandLine bootstrapPython.get(), '-m', 'venv', '.venv'
            }
        }
    }

    void installRuntimeRequirements() {
        File moduleDir = moduleDirectory.get().asFile
        execOperations.exec {
            workingDir = moduleDir
            commandLine getVenvPython().absolutePath, '-m', 'pip', 'install', '-r', requirementsFile.get().asFile.name
        }
    }

    void installDevRequirements() {
        File moduleDir = moduleDirectory.get().asFile
        execOperations.exec {
            workingDir = moduleDir
            commandLine getVenvPython().absolutePath, '-m', 'pip', 'install', '-r', devRequirementsFile.get().asFile.name
        }
    }
}

abstract class PythonTestTask extends PythonVenvTask {
    @OutputFile
    abstract RegularFileProperty getReportFile()

    @TaskAction
    void runPythonTests() {
        File moduleDir = moduleDirectory.get().asFile
        File junitReport = reportFile.get().asFile
        junitReport.parentFile.mkdirs()

        ensureVenv()
        installRuntimeRequirements()

        execOperations.exec {
            workingDir = moduleDir
            commandLine getVenvPython().absolutePath, '-m', 'pytest', "--junitxml=${junitReport.absolutePath}"
        }
    }
}

abstract class PythonQualityTask extends PythonVenvTask {
    @TaskAction
    void runPythonQualityChecks() {
        File moduleDir = moduleDirectory.get().asFile

        ensureVenv()
        installRuntimeRequirements()
        installDevRequirements()

        execOperations.exec {
            workingDir = moduleDir
            commandLine getVenvPython().absolutePath, '-m', 'ruff', 'check', 'python_api_tests', 'tests'
        }

        execOperations.exec {
            workingDir = moduleDir
            commandLine getVenvPython().absolutePath, '-m', 'mypy', 'python_api_tests'
        }
    }
}
