package com.marketplace;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Account implements UserDetails {
    public enum Role { BUYER, SUPPLIER }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 254) private String email;
    @Column(nullable = false, length = 120) private String name;
    @Column(nullable = false, length = 100) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Role role;
    @Column(length = 160) private String company;
    protected Account() {}
    Account(String email, String name, String passwordHash, Role role, String company) {
        this.email=email; this.name=name; this.passwordHash=passwordHash; this.role=role; this.company=company;
    }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Role getRole() { return role; }
    public String getCompany() { return company; }
    public void setName(String value) { name=value; }
    public void setCompany(String value) { company=value; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); }
    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return passwordHash; }
}
