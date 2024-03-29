package ru.otus;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.otus.appcontainer.AppComponentsContainerImpl;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.config.AppConfig;
import ru.otus.services.*;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {"GameProcessor, ru.otus.services.GameProcessor",
            "GameProcessorImpl, ru.otus.services.GameProcessor",
            "gameProcessor, ru.otus.services.GameProcessor",

            "IOService, ru.otus.services.IOService",
            "IOServiceConsole, ru.otus.services.IOService",
            "ioService, ru.otus.services.IOService",

            "PlayerService, ru.otus.services.PlayerService",
            "PlayerServiceImpl, ru.otus.services.PlayerService",
            "playerService, ru.otus.services.PlayerService",

            "EquationPreparer, ru.otus.services.EquationPreparer",
            "EquationPreparerImpl, ru.otus.services.EquationPreparer",
            "equationPreparer, ru.otus.services.EquationPreparer"
    })
    public void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass) throws Exception {
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> clazz = Class.forName("ru.otus.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(clazz);

            component = ctx.getAppComponent(clazz);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        for (var field: component.getClass().getFields()){
            var fieldValue = field.get(component);
            assertThat(fieldValue).isNotNull().isInstanceOfAny(IOService.class, PlayerService.class, EquationPreparer.class);
        }

    }
}