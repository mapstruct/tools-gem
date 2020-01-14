
package nl.bro.dao.common.processor;

import static org.assertj.core.api.Assertions.assertThat;

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

import nl.bro.lib.genericdao.BroRoMetaModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ProcessorTest {

    // CHECKSTYLE:OFF
    @Rule
    public final TemporaryFolder tempDir = new TemporaryFolder(); //NOSONAR
    // CHECKSTYLE:ON

    @Test
    public void example() throws IOException, ClassNotFoundException {
        StringJavaFileObject src = new StringJavaFileObject( "nl.bro.dao.common.processor.BoreholeResearchMapper", getSource() );
        Thread.currentThread().getContextClassLoader().loadClass( BroRoMetaModel.class.getName() );
        compile( new GenericMapperProcessor(), src );
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
        return "package nl.bro.dao.common.processor;\n"
            + "\n"
            + "import nl.bro.dao.common.GenericMapper;\n"
            + "import nl.bro.dao.common.GenericMapperMarker;\n"
            + "import nl.bro.dao.common.impl.generic.testdto.BoreholeResearch;\n"
            + "\n"
            + "@GenericMapperMarker\n"
            + "public interface BoreholeResearchMapper extends GenericMapper<BoreholeResearch> {\n"
            + "}";
    }
}
