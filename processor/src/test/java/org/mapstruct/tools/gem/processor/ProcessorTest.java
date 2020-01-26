/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorTest {

    // CHECKSTYLE:OFF
    @Rule
    public final TemporaryFolder tempDir = new TemporaryFolder(); //NOSONAR
    // CHECKSTYLE:ON

    @Test
    public void example() throws IOException, ClassNotFoundException {
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
        fileManager.setLocation( StandardLocation.CLASS_OUTPUT, Arrays.asList( tempDir.getRoot() ) );

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
}
