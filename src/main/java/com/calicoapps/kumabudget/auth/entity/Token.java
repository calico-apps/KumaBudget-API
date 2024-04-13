package com.calicoapps.kumabudget.auth.entity;

import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.common.Device;
import com.calicoapps.kumabudget.common.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @ManyToOne(fetch = FetchType.LAZY)
    public Credentials credentials;
    @Id
    private String token;

    private boolean refresh;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime expirationDateTime;

    @Enumerated(EnumType.STRING)
    private Device device;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.token.equals(((Token) o).token);
    }

}