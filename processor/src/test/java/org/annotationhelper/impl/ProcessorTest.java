
package org.annotationhelper.impl;

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
        StringJavaFileObject src = new StringJavaFileObject( "org.annotationhelper.test.gem.GemGenerator", getSource() );
        compile( new GemsProcessor(), src );
    }

    private void compile(Processor processor, JavaFileObject... compilationUnits) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager( diagnostics, null, null );
        fileManager.setLocation( StandardLocation.CLASS_OUTPUT, Arrays.asList( tempDir.getRoot() ) );

        JavaCompiler.CompilationTask task = compiler.getTask( null, fileManager, diagnostics, null, null, Arrays.asList( compilationUnits ) );

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

        public StringJavaFileObject(String name, String code) {
            super(
                URI.create( "string:///" + name.replace( '.', '/' ) + Kind.SOURCE.extension ),
                Kind.SOURCE );
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return code;
        }
    }

    private String getSource() {
        return "package org.annotationhelper.test.gem;\n" +
            "\n" +
            "import org.annotationhelper.GemDefinition;\n" +
            "import org.annotationhelper.test.SomeAnnotation;\n" +
            "import org.annotationhelper.test.SomeAnnotations;\n" +
            "\n" +
            "@GemDefinition(value = SomeAnnotation.class)\n" +
            "@GemDefinition(value = SomeAnnotations.class)\n" +
            "@GemDefinition(value = SomeArrayAnnotation.class)\n" +
            "public class GemGenerator {\n" +
            "}";
    }
}
