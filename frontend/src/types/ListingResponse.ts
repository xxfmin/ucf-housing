export interface ListingResponse {
  zpid: string;
  address: string;
  addressStreet: string;
  addressCity: string;
  addressState: string;
  addressZipcode: string;
  latitude: number;
  longitude: number;
  imgSrc: string;
  detailUrl: string;
  statusText: string;
  price: string;
  beds: number;
  baths: number;
  area: number;
  hasVideo: boolean;
  zestimate: number;
  availabilityDate: string | null;
  variableData: string;
  carouselPhotos: string;
  factsAndFeatures: string;
  createdAt: string;
  updatedAt: string;
}

export interface PaginatedResponse {
  content: ListingResponse[];
  pageable: {
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    pageSize: number;
    pageNumber: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
