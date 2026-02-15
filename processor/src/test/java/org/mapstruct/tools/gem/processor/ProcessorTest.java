/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorTest {

    @TempDir
    private File tempDir;

    @Test
    void example() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
            "org.mapstruct.annotations.processor.GemGenerator",
            getSource()
        );
        compile( new GemProcessor(), src );
    }

    private void compile(Processor processor, JavaFileObject... compilationUnits) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager( diagnostics, null, null );
        File classesDir = newFolder( tempDir, "classes" );
        fileManager.setLocation( StandardLocation.CLASS_OUTPUT, Collections.singletonList( classesDir ) );
        File generatedDir = newFolder( tempDir, "generated" );
        fileManager.setLocation( StandardLocation.SOURCE_OUTPUT, Collections.singletonList( generatedDir ) );

        JavaCompiler.CompilationTask task = compiler.getTask(
            null,
            fileManager,
            diagnostics,
            null,
            null,
            Arrays.asList( compilationUnits )
        );

        task.setProcessors( Arrays.asList(
            processor
        ) );

        boolean success = task.call();
        for ( Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics() ) {
            System.err.println( diagnostic );
        }
        assertThat( success ).isTrue();


        assertGeneratedFileContent( "BuilderGem", generatedDir );
        assertGeneratedFileContent( "SomeAnnotationGem", generatedDir );
        assertGeneratedFileContent( "SomeAnnotationsGem", generatedDir );
        assertGeneratedFileContent( "SomeArrayAnnotationGem", generatedDir );
    }

    protected void assertGeneratedFileContent(String gemName, File generatedDir) {
        Path gemPath = generatedDir.toPath().resolve( "org/mapstruct/tools/gem/processor/" + gemName + ".java" );
        assertThat( gemPath )
            .as( gemName )
            .exists();

        try (InputStream generatedGemStream = new FileInputStream( gemPath.toFile() );
             InputStream expectedGemStream = getClass().getClassLoader()
                 .getResourceAsStream( "fixtures/org/mapstruct/tools/gem/processor/" + gemName + ".java" )
        ) {
            assertThat( generatedGemStream )
                .as( gemName )
                .hasSameContentAs( expectedGemStream );
        }
        catch ( IOException e ) {
            throw new UncheckedIOException( "Failed to assert generated content for gem " + gemName, e );
        }

    }

    private static class StringJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        StringJavaFileObject(String name, String code) {
            super(
                URI.create( "string:///" + name.replace( '.', '/' ) + Kind.SOURCE.extension ),
                Kind.SOURCE
            );
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return code;
        }
    }

    private String getSource() {
        return "package org.mapstruct.tools.gem.processor;\n" +
            "\n" +
            "import org.mapstruct.tools.gem.GemDefinition;\n" +
            "import org.mapstruct.tools.gem.test.Builder;\n" +
            "import org.mapstruct.tools.gem.test.SomeAnnotation;\n" +
            "import org.mapstruct.tools.gem.test.SomeAnnotations;\n" +
            "import org.mapstruct.tools.gem.test.gem.SomeArrayAnnotation;\n" +
            "\n" +
            "@GemDefinition(value = SomeAnnotation.class)\n" +
            "@GemDefinition(value = SomeAnnotations.class)\n" +
            "@GemDefinition(value = SomeArrayAnnotation.class)\n" +
            "@GemDefinition(value = Builder.class)\n" +
            "public class GemGenerator {\n" +
            "}";
    }

    private static File newFolder(File root, String folder) throws IOException {
        File result = new File(root, folder);
        if (!result.mkdir()) {
            throw new IOException("Couldn't create folder " + result);
        }
        return result;
    }
}
