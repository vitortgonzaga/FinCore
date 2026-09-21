package com.vitortgonzaga.fincore.account;

import java.time.Instant;
import java.util.UUID;

public record AccountResponse(UUID id, String ownerName, AccountStatus status, Instant createdAt) {
}
