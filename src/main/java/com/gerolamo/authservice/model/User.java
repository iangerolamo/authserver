package com.gerolamo.authservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String password;

  private String name;

  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  private String googleId;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles = new ArrayList<>();

  private boolean enabled;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  public void addRole(Role role) {

    boolean alreadyHas = roles.stream().anyMatch(r -> r.getName().equals(role.getName()));
    if (!alreadyHas) {
      this.roles.add(role);
    }

  }
}
