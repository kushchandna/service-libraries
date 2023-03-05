package com.kush.messaging_old.content;

import java.io.Serializable;

import com.kush.service.annotations.Exportable;
import com.kush.utils.commons.AssociatedClasses;

@AssociatedClasses({ TextContent.class })
@Exportable
public interface Content extends Serializable {
}
