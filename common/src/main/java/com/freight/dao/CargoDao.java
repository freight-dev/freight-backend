package com.freight.dao;

import com.freight.model.BulkType;
import com.freight.model.Cargo;
import com.freight.model.CargoType;
import com.freight.model.ContainerType;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.freight.dao.BaseDao.Sort.DESC;
import static java.util.Objects.requireNonNull;

/**
 * Created by toshikijahja on 6/7/17.
 */
public class CargoDao extends BaseDao<Cargo> {

    @AssistedInject
    public CargoDao(@Assisted final SessionProvider sessionProvider) {
        super(sessionProvider, Cargo.class);
    }

    public Cargo createCargo(final int userId,
                             final CargoType cargoType,
                             final int quantity,
                             final Instant departure,
                             final Optional<Integer> weightOptional,
                             final Cargo.WeightUnit weightUnit,
                             final Optional<Integer> volumeOptional,
                             final Cargo.VolumeUnit volumeUnit,
                             final Optional<Integer> lengthOptional,
                             final Optional<Integer> widthOptional,
                             final Optional<Integer> heightOptional,
                             final Cargo.DimensionUnit dimensionUnit,
                             final Optional<ContainerType> containerTypeOptional,
                             final Optional<BulkType> bulkTypeOptional) {
        getSessionProvider().startTransaction();
        final Cargo cargo = new Cargo.Builder()
                .userId(userId)
                .cargoType(cargoType)
                .quantity(quantity)
                .departure(departure)
                .weight(weightOptional.map(weight -> weight).orElse(null))
                .weightUnit(weightUnit)
                .volume(volumeOptional.map(volume -> volume).orElse(null))
                .volumeUnit(volumeUnit)
                .length(lengthOptional.map(length -> length).orElse(null))
                .width(widthOptional.map(width -> width).orElse(null))
                .height(heightOptional.map(height -> height).orElse(null))
                .dimensionUnit(dimensionUnit)
                .containerType(containerTypeOptional.map(containerType -> containerType).orElse(null))
                .bulkType(bulkTypeOptional.map(bulkType -> bulkType).orElse(null))
                .build();
        getSessionProvider().getSession().persist(cargo);
        getSessionProvider().commitTransaction();
        return cargo;
    }

    public List<Cargo> getByUserIdAndStatusSortedAndPaginated(final int userId,
                                                              final Cargo.Status status,
                                                              final int start,
                                                              final int limit) {
        requireNonNull(status);

        final Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("userId", userId);
        inputParam.put("status", status);

        return getByFieldSortedAndPaginated("userId = :userId AND status = :status", inputParam, "id", DESC, start, limit);
    }
}
