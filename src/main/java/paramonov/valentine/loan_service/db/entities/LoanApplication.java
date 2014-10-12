package paramonov.valentine.loan_service.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "APPLICATION")
public class LoanApplication {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER")
    private User user;

    @Column(name = "AMOUNT")
    private BigDecimal loanAmount;

    @Column(name = "INTEREST")
    private BigDecimal loanInterest;

    @Column(name = "DUE_DATE")
    private Date dueDate;

    public Long getId() {
        return id;
    }

    public LoanApplication setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public LoanApplication setUser(User user) {
        this.user = user;
        return this;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public LoanApplication setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
        return this;
    }

    public BigDecimal getLoanInterest() {
        return loanInterest;
    }

    public LoanApplication setLoanInterest(BigDecimal loanInterest) {
        this.loanInterest = loanInterest;
        return this;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public LoanApplication setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("LoanApplication{");
        sb.append("id=").append(id);
        sb.append(", user=").append(user);
        sb.append(", loanAmount=").append(loanAmount);
        sb.append(", loanInterest=").append(loanInterest);
        sb.append(", dueDate=").append(dueDate);
        sb.append('}');

        return sb.toString();
    }
}
