package paramonov.valentine.loan_service.common.dtos;

import paramonov.valentine.loan_service.common.LoanEventStatus;

import java.util.Date;

public class LoanEventDto {
    private Long applicationId;

    private Date eventDate;

    private LoanEventStatus eventStatus;

    public Long getApplicationId() {
        return applicationId;
    }

    public LoanEventDto withApplicationId(Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public LoanEventDto withEventDate(Date eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public LoanEventStatus getEventStatus() {
        return eventStatus;
    }

    public LoanEventDto withEventStatus(LoanEventStatus eventStatus) {
        this.eventStatus = eventStatus;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder("LoanEventDto{")
            .append("applicationId=").append(applicationId)
            .append(", eventDate=").append(eventDate)
            .append(", eventStatus=").append(eventStatus)
            .append('}')
            .toString();
    }
}
