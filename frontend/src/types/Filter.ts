export interface FilterState {
  priceRange: [number, number];
  areaRange: [number, number];
  cities: string[];
  zipCodes: string[];
  bedrooms: string;
  bathrooms: string;
  propertyTypes: string[];
  availabilityDate: Date | undefined;
}

export const defaultFilters: FilterState = {
  priceRange: [600, 15000],
  areaRange: [150, 5100],
  cities: [],
  zipCodes: [],
  bedrooms: "any",
  bathrooms: "any",
  propertyTypes: [],
  availabilityDate: undefined,
};

export const cities = [
  { value: "Orlando", label: "Orlando" },
  { value: "Oviedo", label: "Oviedo" },
  { value: "Winter Park", label: "Winter Park" },
  { value: "Winter Springs", label: "Winter Springs" },
];

export const zipCodes = [
  "32708",
  "32765",
  "32766",
  "32792",
  "32817",
  "32820",
  "32824",
  "32825",
  "32826",
  "32827",
  "32828",
  "32829",
  "32833",
];

export const propertyTypes = [
  { id: "apartment", label: "Apartment" },
  { id: "condo", label: "Condo" },
  { id: "townhouse", label: "Townhouse" },
  { id: "house", label: "House" },
];

export const bedroomOptions = [
  { value: "any", label: "Any" },
  { value: "0", label: "Studio" },
  { value: "1", label: "1 bed" },
  { value: "2", label: "2 beds" },
  { value: "3", label: "3 beds" },
  { value: "4", label: "4 beds" },
  { value: "5", label: "5 beds" },
  { value: "6", label: "6 beds" },
  { value: "7", label: "7 beds" },
  { value: "8", label: "8+ beds" },
];

export const bathroomOptions = [
  { value: "any", label: "Any" },
  { value: "1", label: "1 bath" },
  { value: "2", label: "2 baths" },
  { value: "3", label: "3 baths" },
  { value: "4", label: "4 baths" },
  { value: "5", label: "5 baths" },
  { value: "6", label: "6+ baths" },
];
