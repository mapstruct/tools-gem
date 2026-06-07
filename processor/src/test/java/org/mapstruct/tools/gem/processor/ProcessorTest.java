/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.tools.gem.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ProcessorTest {

    @TempDir
    private File tempDir;

    @Test
    void example() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
            "org.mapstruct.annotations.processor.GemGenerator",
            getSource()
        );
        File generatedDir = compile( new GemProcessor(), src );
        assertGeneratedFileContent( "BuilderGem", generatedDir );
        assertGeneratedFileContent( "CustomBuilderGem", generatedDir );
        assertGeneratedFileContent( "SomeAnnotationGem", generatedDir );
        assertGeneratedFileContent( "SomeAnnotationsGem", generatedDir );
        assertGeneratedFileContent( "SomeArrayAnnotationGem", generatedDir );
    }

    @Test
    void singleAnnotation() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.GemGenerator",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.GemDefinition;\n" +
                        "import org.mapstruct.tools.gem.test.Builder;\n" +
                        "\n" +
                        "@GemDefinition(value = Builder.class)\n" +
                        "public class GemGenerator {\n" +
                        "}"
        );
        File generatedDir = compile( new GemProcessor(), src );
        assertGeneratedFileContent( "BuilderGem", generatedDir );
    }

    @Test
    void generateEnum() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.GemGenerator",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.GemDefinition;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.EnumAnnotation;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.SimpleEnum;\n" +
                        "\n" +
                        "@GemDefinition(value = SimpleEnum.class)\n" +
                        "@GemDefinition(value = EnumAnnotation.class)\n" +
                        "public class GemGenerator {\n" +
                        "}"
        );
        File generatedDir = compile( new GemProcessor(), src );
        assertGeneratedFileContent( "SimpleEnumGem", generatedDir );
        assertGeneratedFileContent( "EnumAnnotationGem", generatedDir );
    }

    @Test
    void generateEnumWithRegisterGem() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.GemGenerator",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.GemDefinition;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.EnumAnnotation;\n" +
                        "\n" +
                        "@GemDefinition(value = EnumAnnotation.class," +
                        " implementationName = \"<CLASS_NAME>RegisterGem\")\n" +
                        "public class GemGenerator {\n" +
                        "}"
        );
        File generatedDir = compile( new GemProcessor(), src, getManuelGemOfEnum() );
        assertGeneratedFileContent( "EnumAnnotationRegisterGem", generatedDir );
    }

    @Test
    void generateEnumWithRegisterOtherPackageGem() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.GemGenerator",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.GemDefinition;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.EnumAnnotation;\n" +
                        "\n" +
                        "@GemDefinition(value = EnumAnnotation.class," +
                        " implementationName = \"<CLASS_NAME>PackageGem\")\n" +
                        "public class GemGenerator {\n" +
                        "}"
        );
        StringJavaFileObject enumSr = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.other.MySimpleEnumGem",
                "package org.mapstruct.tools.gem.processor.other;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.RegisterGem;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.SimpleEnum;\n" +
                        "\n" +
                        "@RegisterGem(value = SimpleEnum.class)\n" +
                        "public enum MySimpleEnumGem {\n" +
                        "A,B,C;\n" +
                        "}"
        );
        File generatedDir = compile( new GemProcessor(), enumSr, src );
        assertGeneratedFileContent( "EnumAnnotationPackageGem", generatedDir );
    }

    @Test
    void registerGem() throws IOException {
        compile( new GemProcessor(), getManuelGemOfEnum() );
    }

    @Test
    void registerGemMissingEnumConstants() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.MySimpleEnumGem",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.RegisterGem;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.SimpleEnum;\n" +
                        "\n" +
                        "@RegisterGem(value = SimpleEnum.class)\n" +
                        "public enum MySimpleEnumGem {\n" +
                        "C;\n" +
                        "}"
        );
        List<Diagnostic<? extends JavaFileObject>> diagnostics = failCompile( new GemProcessor(), src );
        assertThat( diagnostics ).singleElement()
                .extracting(
                        Diagnostic::getKind,
                        d -> d.getMessage( null )
                )
                .containsExactly(
                        Diagnostic.Kind.ERROR,
                        "Enum constants [A, B] are missing in org.mapstruct.tools.gem.processor.MySimpleEnumGem." +
                                " A enum gem of org.mapstruct.tools.gem.test.enummapping.SimpleEnum should exactly" +
                                " contain [A, B, C]" );
    }

    @Test
    void registerGemMoreEnumConstants() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.MySimpleEnumGem",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.RegisterGem;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.SimpleEnum;\n" +
                        "\n" +
                        "@RegisterGem(value = SimpleEnum.class)\n" +
                        "public enum MySimpleEnumGem {\n" +
                        "A,B,C,D,E;\n" +
                        "}"
        );
        List<Diagnostic<? extends JavaFileObject>> diagnostics = failCompile( new GemProcessor(), src );
        assertThat( diagnostics ).singleElement()
                .extracting(
                        Diagnostic::getKind,
                        d -> d.getMessage( null )
                )
                .containsExactly(
                        Diagnostic.Kind.ERROR,
                        "Enum constants [D, E] are only present in org.mapstruct.tools.gem.processor.MySimpleEnumGem." +
                                " A enum gem of org.mapstruct.tools.gem.test.enummapping.SimpleEnum should exactly" +
                                " contain [A, B, C]"
                );
    }

    @Test
    void registerEnumTwiceGeneratorFirst() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.GemGenerator",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.GemDefinition;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.SimpleEnum;\n" +
                        "\n" +
                        "@GemDefinition(value = SimpleEnum.class)\n" +
                        "public class GemGenerator {\n" +
                        "}"
        );
        List<Diagnostic<? extends JavaFileObject>> diagnostics = failCompile( new GemProcessor(),
                src, getManuelGemOfEnum() );
        assertThat( diagnostics ).singleElement()
                .extracting(
                        Diagnostic::getKind,
                        d -> d.getMessage( null )
                )
                .containsExactly(
                        Diagnostic.Kind.ERROR,
                        "Enum gem org.mapstruct.tools.gem.test.enummapping.SimpleEnum can only be registered once"
                );
    }

    @Test
    void registerEnumTwiceGemDefinitionFirst() throws IOException {
        StringJavaFileObject src = new StringJavaFileObject(
                "org.mapstruct.annotations.processor.GemGenerator",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.GemDefinition;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.SimpleEnum;\n" +
                        "\n" +
                        "@GemDefinition(value = SimpleEnum.class)\n" +
                        "public class GemGenerator {\n" +
                        "}"
        );
        List<Diagnostic<? extends JavaFileObject>> diagnostics = failCompile( new GemProcessor(),
                getManuelGemOfEnum(), src );
        assertThat( diagnostics ).singleElement()
                .extracting(
                        Diagnostic::getKind,
                        d -> d.getMessage( null )
                )
                .containsExactly(
                        Diagnostic.Kind.ERROR,
                        "Enum gem org.mapstruct.tools.gem.test.enummapping.SimpleEnum can only be registered once"
                );
    }

    private List<Diagnostic<? extends JavaFileObject>> failCompile(Processor processor,
                                                                   JavaFileObject... compilationUnits)
            throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
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

        task.setProcessors( Collections.singletonList(
                processor
        ) );

        assertThat( task.call() ).isFalse();
        return diagnostics.getDiagnostics();
    }

    private File compile(Processor processor, JavaFileObject... compilationUnits) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
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

        task.setProcessors( Collections.singletonList(
                processor
        ) );

        boolean success = task.call();
        for ( Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics() ) {
            System.err.println( diagnostic );
        }
        assertThat( success ).isTrue();
        return generatedDir;
    }

    protected void assertGeneratedFileContent(String gemName, File generatedDir) {
        Path gemPath = generatedDir.toPath().resolve( "org/mapstruct/tools/gem/processor/" + gemName + ".java" );
        assertThat( gemPath )
            .as( gemName )
            .exists();

        try (InputStream generatedGemStream = Files.newInputStream( gemPath.toFile().toPath() );
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
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
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
            "@GemDefinition(value = Builder.class, implementationName = \"Custom<CLASS_NAME>Gem\")\n" +
            "public class GemGenerator {\n" +
            "}";
    }

    private static StringJavaFileObject getManuelGemOfEnum() {
        return new StringJavaFileObject(
                "org.mapstruct.annotations.processor.MySimpleEnumGem",
                "package org.mapstruct.tools.gem.processor;\n" +
                        "\n" +
                        "import org.mapstruct.tools.gem.RegisterGem;\n" +
                        "import org.mapstruct.tools.gem.test.enummapping.SimpleEnum;\n" +
                        "\n" +
                        "@RegisterGem(value = SimpleEnum.class)\n" +
                        "public enum MySimpleEnumGem {\n" +
                        "A,B,C;\n" +
                        "}"
        );
    }

    private static File newFolder(File root, String folder) throws IOException {
        File result = new File(root, folder);
        if (!result.mkdir()) {
            throw new IOException("Couldn't create folder " + result);
        }
        return result;
    }
}
