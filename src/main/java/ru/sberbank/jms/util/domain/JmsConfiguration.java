package ru.sberbank.jms.util.domain;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.plural.RooPlural;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooPlural("Options")
public class JmsConfiguration {

    /**
     */
    @NotNull
    @Size(min = 3)
    private String url;

    /**
     */
    @Id
    @NotNull
    @NotBlank
    private String configurationName;

    /**
     */
    @Size(min = 3)
    private String queueName;

    /**
     */
    @NotNull
    @Min(5L)
    @Max(999999L)
    private int delay;

    /**
     */
    @Size(min = 3)
    private String queueNameReceive;
}
