package paramonov.valentine.loan_service.properties.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.Resource;
import paramonov.valentine.loan_service.properties.editors.TimeEditor;
import paramonov.valentine.loan_service.util.Time;

import java.beans.PropertyEditor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource({"classpath:res/loan_service.properties", "classpath:res/jdbc.properties"})
class PropertyPlaceholderConfig {
    @Bean
    @Autowired
    public static PropertyResourceConfigurer propertyResourceConfigurer(StringEncryptor encryptor) {
        final EncryptablePropertyPlaceholderConfigurer configurer =
            new EncryptablePropertyPlaceholderConfigurer(encryptor);
        final Resource[] resourceLocations = getLocations();

        configurer.setLocations(resourceLocations);

        return configurer;
    }

    @Bean
    public StringEncryptor encryptor() {
        final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("pass@word");

        return encryptor;
    }

    @Bean
    public static CustomEditorConfigurer editorConfigurer() {
        final CustomEditorConfigurer configurer = new CustomEditorConfigurer();
        final Map<Class<?>, Class<? extends PropertyEditor>> editors = new HashMap<>();
        final Map<Class<?>, Class<? extends PropertyEditor>> immutableEditors = Collections.unmodifiableMap(editors);

        editors.put(Time.class, TimeEditor.class);
        configurer.setCustomEditors(immutableEditors);

        return configurer;
    }

    private static Resource[] getLocations() {
        final String[] sources = getSources();
        if(sources == null || sources.length == 0) {
            return new Resource[0];
        }

        return getResources(sources);
    }

    private static Resource[] getResources(String[] sources) {
        final StaticApplicationContext context = new StaticApplicationContext();
        final int numSources = sources.length;
        final Resource[] resources = new Resource[numSources];
        for(int i = 0; i < numSources; i++) {
            final String source = sources[i];
            resources[i] = context.getResource(source);
        }

        return resources;
    }

    private static String[] getSources() {
        final Class<PropertyPlaceholderConfig> configClass = PropertyPlaceholderConfig.class;
        final PropertySource propertySourceAnnotation = configClass.getAnnotation(PropertySource.class);

        return (String[]) propertySourceAnnotation.value();
    }
}
