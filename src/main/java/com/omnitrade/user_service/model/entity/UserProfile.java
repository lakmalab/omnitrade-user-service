package com.omnitrade.user_service.model.entity;

import com.omnitrade.user_service.model.enums.UserRole;
import com.omnitrade.user_service.model.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "user_profiles",
        indexes = {
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_city", columnList = "city")
        }
)
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "auth_user_id", length = 36)
    private UUID authUserId;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(nullable = false, unique = true)
    private String username ;

    private String firstName ;
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = true, unique = true)
    private String phone;
    private String city;
    private String district;
    private String profileImage;
    private Double rating;
    private int reviewCount;
    private Integer totalSales = 0;
    private Integer totalTrades = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant joinedAt;

    @UpdateTimestamp
    private Instant updatedAt;
    private Boolean phoneVerified = false;
    private Boolean emailVerified = false;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;
}
