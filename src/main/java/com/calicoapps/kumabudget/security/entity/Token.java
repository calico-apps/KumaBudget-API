package com.calicoapps.kumabudget.security.entity;

import com.calicoapps.kumabudget.common.util.JsonUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auth_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @ManyToOne(fetch = FetchType.LAZY)
    public Credentials credentials;
    @Id
    private String token;
    private boolean revoked;
    private boolean expired;
    private boolean refresh;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.token.equals(((Token) o).token);
    }

}