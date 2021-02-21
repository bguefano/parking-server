package bguefano.parkings.provider;

import bguefano.parkings.domain.Coordinates;
import bguefano.parkings.domain.ParkingInfos;
import bguefano.parkings.domain.bm.BMFeatureMember;

public class BMDeserializer implements ParkingInfos.Deserializer<BMFeatureMember> {

    @Override
    public ParkingInfos toParkingInfos(BMFeatureMember member) {
        return ParkingInfos.builder()
                .name(member.getStparkp().getName())
                .position(Coordinates.of(member.getStparkp().getGeometry().getPoint().getPos()))
                .capacity(member.getStparkp().getTotal())
                .available(member.getStparkp().getLibres())
                .build();
    }
}
