/**
 * 
 */
package net.paramount.entity.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.paramount.auth.entity.UserAccountProfile;
import net.paramount.entity.contact.ContactAddress;
import net.paramount.entity.contact.ContactAssignment;
import net.paramount.entity.contact.ContactCommunication;
import net.paramount.entity.contact.ContactCore;
import net.paramount.entity.contact.ContactHierarchy;
import net.paramount.entity.contact.ContactProfile;
import net.paramount.entity.general.Money;
import net.paramount.entity.general.MoneySet;
import net.paramount.entity.general.Quantity;
import net.paramount.entity.stock.InventoryDetail;
import net.paramount.framework.entity.RepoEntity;

/**
 * @author ducbq
 *
 */
@Builder
@Data
@Entity
@Table(name="ORDER_ITEM")
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends RepoEntity {
	private static final long serialVersionUID = -4930122848308077532L;

  @ManyToOne
  @JoinColumn(name="OWNER_ID")
  private OrderNote owner;
  
  @ManyToOne
  @JoinColumn(name="PRODUCT_ID")
  private InventoryDetail product;
  
  @Column(name="LINE_CODE", length=10)
  private String lineCode;
  
  @Column(name="INFO")
  private String info;

  @Builder.Default
  @Embedded
  @Valid
  private Quantity quantity = new Quantity();
  
  @Builder.Default
  @Column(name="APPROVED_QUANTITY", precision=5, scale=2)
  private Double approvedQuantity = 0d;
  
  @Builder.Default
  @Column(name="CLOSED_QUANTITY", precision=5, scale=2)
  private Double closedQuantity = 0d;
  
  @Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="PRICECCY")),
      @AttributeOverride(name="value", column=@Column(name="PRICEVALUE"))
  })
  private Money unitPrice = new Money(); 
  
  @Builder.Default
  @Embedded
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="CCY")),
      @AttributeOverride(name="value",    column=@Column(name="CCYVAL")),
      @AttributeOverride(name="localAmount", column=@Column(name="LCYVAL"))
  })
  private MoneySet amount = new MoneySet(); 
  
  @Builder.Default
  @Column(name="TAX_RATE", precision=10, scale=2)
  private BigDecimal taxRate = BigDecimal.ZERO;
  
  @Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="TAXCCY")),
      @AttributeOverride(name="value", column=@Column(name="TAXVALUE")),
      @AttributeOverride(name="localAmount", column=@Column(name="TAXLCYVAL"))
  })
  private MoneySet taxAmount = new MoneySet(); 
  
  @Builder.Default
  @Embedded
  @Valid
  @AttributeOverrides( {
      @AttributeOverride(name="currency", column=@Column(name="TOTALCCY")),
      @AttributeOverride(name="value", column=@Column(name="TOTALVALUE")),
      @AttributeOverride(name="localAmount", column=@Column(name="TOTALLCYVAL"))
  })
  private Money totalAmaount = new Money();
}
