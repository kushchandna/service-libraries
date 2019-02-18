package com.kush.lib.questionnaire.persistance;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.questionnaire.core.Option;
import com.kush.utils.id.Identifier;

public interface OptionPersistor extends Persistor<Option> {

    Option addOption(Identifier questionId, String content, Identifier addedBy);
}
