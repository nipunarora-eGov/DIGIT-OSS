/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 *
 * Copyright (C) <2015> eGovernments Foundation
 *
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 *
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 *
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 *
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 *
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 *
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.application.repository;

import java.util.Date;
import java.util.List;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.wtms.application.entity.MeterReadingConnectionDetails;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterConnectionDetailsRepository extends JpaRepository<WaterConnectionDetails, Long> {

    WaterConnectionDetails findByApplicationNumber(String applicationNumber);

    WaterConnectionDetails findByApplicationNumberAndConnectionStatus(String applicationNumber,
            ConnectionStatus connectionStatus);

    List<WaterConnectionDetails> findAllByApplicationDateOrderByApplicationNumberAsc(Date applicationDate);

    List<WaterConnectionDetails> findAllByApplicationDateAndConnectionStatusOrderByApplicationNumberAsc(
            Date applicationDate, ConnectionStatus connectionStatus);

    List<WaterConnectionDetails> findAllByApplicationTypeOrderByApplicationNumberAsc(ApplicationType applicationType);

    List<WaterConnectionDetails> findAllByApplicationTypeAndConnectionStatusOrderByApplicationNumberAsc(
            ApplicationType applicationType, ConnectionStatus connectionStatus);

    WaterConnectionDetails findByApplicationNumberOrConnection_ConsumerCode(String applicationNumber,
            String consumerCode);

    WaterConnectionDetails findByConnection_ConsumerCodeAndConnectionStatus(String consumerCode,
            ConnectionStatus connectionStatus);

    WaterConnectionDetails findByConnection(WaterConnection waterConnection);

    WaterConnectionDetails findByConnectionAndConnectionStatus(WaterConnection waterConnection,
            ConnectionStatus connectionStatus);

    @Query("select wcd from WaterConnectionDetails wcd where wcd.applicationNumber=:applicationNumber and wcd.demand.egInstallmentMaster=:installment")
    WaterConnectionDetails findByApplicationNumberAndInstallment(@Param("installment") Installment installment,
            @Param("applicationNumber") String applicationNumber);

    // TODO - Need to re-check this query once closure of connection is
    // implemented. I.e. Whether we allow to close additional
    // TODO - .. connections also when closure of Primary connection happens.
    // Fixme Later : We are assuming that there will be only one primary
    // connection for given property ID other than INACTIVE and CLOSED status
    @Query("select wcd from WaterConnectionDetails wcd where wcd.connection.parentConnection is null and wcd.connectionStatus not in ('INACTIVE', 'CLOSED') and wcd.connection.propertyIdentifier =:propertyIdentifier")
    WaterConnectionDetails getPrimaryConnectionDetailsByPropertyID(
            @Param("propertyIdentifier") String propertyIdentifier);

    @Query(" from WaterConnectionDetails WCD where WCD.connectionStatus in(:status)"
            + " and WCD.connection.propertyIdentifier =:propertyIdentifier")
    WaterConnectionDetails getConnectionDetailsInWorkflow(
            @Param("propertyIdentifier") String propertyIdentifier, @Param("status") ConnectionStatus Status);

    WaterConnectionDetails findByConnection_PropertyIdentifierAndConnectionStatusAndConnection_ParentConnectionIsNull(
            String propertyIdentifier, ConnectionStatus connectionStatus);

    WaterConnectionDetails findByDemand(EgDemand demand);

    @Query("select wcd from MeterReadingConnectionDetails wcd where wcd.waterConnectionDetails.id=:waterConnDetId order by currentReadingDate desc")
    List<MeterReadingConnectionDetails> findPreviousMeterReadingReading(@Param("waterConnDetId") Long waterConnDetId);

    @Query("select wcd from WaterConnectionDetails wcd  where wcd.connection.id  in (select wc.id from WaterConnection wc where wc.parentConnection.id = :parentId) ")
    List<WaterConnectionDetails> getAllConnectionDetailsByParentConnection(@Param("parentId") Long parentId);
    
    WaterConnectionDetails findByConnectionAndConnectionStatusAndIsHistory(WaterConnection waterConnection,
            ConnectionStatus connectionStatus, Boolean isHistory);
}
