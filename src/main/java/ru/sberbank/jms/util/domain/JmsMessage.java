package ru.sberbank.jms.util.domain;
import org.hibernate.metamodel.source.annotations.attribute.type.LobTypeResolver;
import org.hibernate.validator.constraints.Length;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class JmsMessage {

    /**
     */
    @Lob
    @Column(columnDefinition = "CLOB",length = 5020000)
    @NotNull
    private String messageBody;
}
