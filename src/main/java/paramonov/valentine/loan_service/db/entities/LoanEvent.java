package paramonov.valentine.loan_service.db.entities;

import paramonov.valentine.loan_service.common.LoanEventStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "EVENT")
public class LoanEvent {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER")
    private User user;

    @ManyToOne
    @JoinColumn(name = "APPLICATION")
    private LoanApplication application;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private LoanEventStatus eventStatus;

    @Column(name = "EVENT_DATE")
    private Date eventDate;

    public Long getId() {
        return id;
    }

    public LoanEvent setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public LoanEvent setUser(User user) {
        this.user = user;
        return this;
    }

    public LoanApplication getApplication() {
        return application;
    }

    public LoanEvent setApplication(LoanApplication application) {
        this.application = application;
        return this;
    }

    public LoanEventStatus getEventStatus() {
        return eventStatus;
    }

    public LoanEvent setEventStatus(LoanEventStatus eventStatus) {
        this.eventStatus = eventStatus;
        return this;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public LoanEvent setEventDate(Date eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoanEvent{");
        sb.append("id=").append(id);
        sb.append(", user=").append(user);
        sb.append(", application=").append(application);
        sb.append(", eventStatus=").append(eventStatus);
        sb.append(", eventDate=").append(eventDate);
        sb.append('}');

        return sb.toString();
    }
}
