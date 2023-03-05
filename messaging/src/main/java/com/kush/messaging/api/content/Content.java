package com.kush.messaging.api.content;

import java.io.Serializable;

import com.kush.service.annotations.Exportable;
import com.kush.utils.commons.AssociatedClasses;

@AssociatedClasses({ TextContent.class, LocationUpdateContent.class })
@Exportable
public interface Content extends Serializable {
}
