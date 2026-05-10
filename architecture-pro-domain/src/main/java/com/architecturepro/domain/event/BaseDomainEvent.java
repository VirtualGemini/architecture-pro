package com.architecturepro.domain.event;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 领域事件基类
 * <p>
 * 领域事件表示领域中已经发生的、具有业务含义的事件
 */
public abstract class BaseDomainEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 事件ID */
    private String eventId;

    /** 事件发生时间 */
    private LocalDateTime occurredOn;

    /** 事件类型 */
    private String eventType;

    protected BaseDomainEvent() {
        this.occurredOn = LocalDateTime.now();
        this.eventType = this.getClass().getSimpleName();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    public void setOccurredOn(LocalDateTime occurredOn) {
        this.occurredOn = occurredOn;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
