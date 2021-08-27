import React, { Fragment } from "react";
import { Card, CardHeader, CardLabel, CardText, CitizenInfoLabel, Loader, SubmitBar } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

const StakeholderDocsRequired = ({ onSelect, onSkip, config }) => {
  const { t } = useTranslation();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const state = tenantId.split(".")[0];
  const history = useHistory();
  const { data, isLoading } = Digit.Hooks.obps.useMDMS(state, "StakeholderRegistraition", "TradeTypetoRoleMapping");


  if (isLoading) {
    return (
      <Loader />
    )
  }

  return (
    <Fragment>
      <Card>
        <CardHeader>{t(`BPA_NEW_BUILDING_HEADER`)}</CardHeader>
        <CitizenInfoLabel text={t(`OBPS_DOCS_REQUIRED_TIME`)} showInfo={false} />
        <CardText style={{ color: "#0B0C0C", marginTop: "12px" }}>{t(`BPA_NEW_BUILDING_PERMIT_DESCRIPTION`)}</CardText>
        {isLoading ?
          <Loader /> :
          <Fragment>
            {data?.StakeholderRegistraition?.TradeTypetoRoleMapping?.[0]?.docTypes?.map((doc, index) => (
              <CardLabel style={{ fontWeight: 700 }} key={index}>{`${index + 1}. ${t(doc?.code.replace('.', '_'))}`}</CardLabel>
            ))}
          </Fragment>
        }
        <SubmitBar label={t(`CS_COMMON_NEXT`)} onSubmit={onSelect} />
      </Card>
      <CitizenInfoLabel text={t(`OBPS_DOCS_FILE_SIZE`)} />
    </Fragment>
  );
};

export default StakeholderDocsRequired;