package com.kush.messaging.content;

import java.io.Serializable;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.commons.AssociatedClasses;

@AssociatedClasses({ TextContent.class })
@Exportable
public interface Content extends Serializable {
}
