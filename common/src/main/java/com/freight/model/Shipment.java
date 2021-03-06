package com.freight.model;

import com.freight.exception.FreightException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

import static com.freight.exception.BadRequest.SHIPMENT_STATUS_NOT_EXIST;

/**
 * Created by toshikijahja on 11/7/18.
 */
@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipId")
    private Ship ship;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "originLocationId")
    private Location originLocation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destinationLocationId")
    private Location destinationLocation;

    @Column
    private Instant departure;

    @Column
    private Instant arrival;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    @CreationTimestamp
    private Instant created;

    @Column
    @UpdateTimestamp
    private Instant lastModified;

    public enum Status {
        UPCOMING,
        LIVE,
        COMPLETED,
        CANCELED;

        public static Status getStatus(final String statusInString) {
            for (final Status status : Status.values()) {
                if (status.name().equalsIgnoreCase(statusInString)) {
                    return status;
                }
            }
            throw new FreightException(SHIPMENT_STATUS_NOT_EXIST);
        }
    }

    public Shipment() {}

    private Shipment(final Builder builder) {
        this.id = builder.id;
        this.ship = builder.ship;
        this.originLocation = builder.originLocation;
        this.destinationLocation = builder.destinationLocation;
        this.departure = builder.departure;
        this.arrival = builder.arrival;
        this.status = builder.status;
    }

    public int getId() {
        return id;
    }

    public Ship getShip() {
        return ship;
    }

    public Location getOriginLocation() {
        return originLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public Instant getDeparture() {
        return departure;
    }

    public Instant getArrival() {
        return arrival;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public static class Builder {
        private int id;
        private Ship ship;
        private Location originLocation;
        private Location destinationLocation;
        private Instant departure;
        private Instant arrival;
        private Status status = Status.UPCOMING;

        public Builder id(final int id) {
            this.id = id;
            return this;
        }

        public Builder ship(final Ship ship) {
            this.ship = ship;
            return this;
        }

        public Builder originLocation(final Location originLocation) {
            this.originLocation = originLocation;
            return this;
        }

        public Builder destinationLocation(final Location destinationLocation) {
            this.destinationLocation = destinationLocation;
            return this;
        }

        public Builder departure(final Instant departure) {
            this.departure = departure;
            return this;
        }

        public Builder arrival(final Instant arrival) {
            this.arrival = arrival;
            return this;
        }

        public Builder status(final Status status) {
            this.status = status;
            return this;
        }

        public Shipment build() {
            return new Shipment(this);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(ship)
                .append(originLocation)
                .append(destinationLocation)
                .append(departure)
                .append(arrival)
                .append(status)
                .append(created)
                .append(lastModified)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Shipment that = (Shipment) o;
        return new EqualsBuilder()
                .append(id, that.id)
                .append(ship, that.ship)
                .append(originLocation, that.originLocation)
                .append(destinationLocation, that.destinationLocation)
                .append(departure, that.departure)
                .append(arrival, that.arrival)
                .append(status, that.status)
                .append(created, that.created)
                .append(lastModified, that.lastModified)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("id", id)
                .append("ship", ship)
                .append("originLocation", originLocation)
                .append("destinationLocation", destinationLocation)
                .append("departure", departure)
                .append("arrival", arrival)
                .append("status", status)
                .append("created", created)
                .append("lastModified", lastModified)
                .toString();
    }
}
