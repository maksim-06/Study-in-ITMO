package Common.domein;


import java.io.Serializable;

// перечисление всех типов
public enum OrganizationType implements Serializable {
    COMMERCIAL,
    GOVERNMENT,
    TRUST,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;
}