package com.kush.lib.questionnaire.persistance;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.questionnaire.core.Option;
import com.kush.utils.id.Identifier;

public class DefaultOptionPersistor extends DelegatingPersistor<Option> implements OptionPersistor {

    public DefaultOptionPersistor(Persistor<Option> delegate) {
        super(delegate);
    }

    @Override
    public Option addOption(Identifier questionId, String content, Identifier addedBy) throws PersistorOperationFailedException {
        Option option = new Option(questionId, content, addedBy);
        return save(option);
    }
}
