export function buildApiUrl(
  searchParams: Record<string, string | string[]>
): string {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL || "http://localhost:4000";
  const url = new URL("/listings", baseUrl);

  // Handle price range filters
  if (searchParams.minPrice) {
    url.searchParams.set(
      "minPrice",
      Array.isArray(searchParams.minPrice)
        ? searchParams.minPrice[0]
        : searchParams.minPrice
    );
  }
  if (searchParams.maxPrice) {
    url.searchParams.set(
      "maxPrice",
      Array.isArray(searchParams.maxPrice)
        ? searchParams.maxPrice[0]
        : searchParams.maxPrice
    );
  }

  // Handle area range filters
  if (searchParams.minArea) {
    url.searchParams.set(
      "minArea",
      Array.isArray(searchParams.minArea)
        ? searchParams.minArea[0]
        : searchParams.minArea
    );
  }
  if (searchParams.maxArea) {
    url.searchParams.set(
      "maxArea",
      Array.isArray(searchParams.maxArea)
        ? searchParams.maxArea[0]
        : searchParams.maxArea
    );
  }

  // Handle location filters (arrays)
  if (searchParams.cities) {
    const cities = Array.isArray(searchParams.cities)
      ? searchParams.cities
      : [searchParams.cities];
    cities.forEach((city) => url.searchParams.append("cities", city));
  }

  if (searchParams.zipCodes) {
    const zipCodes = Array.isArray(searchParams.zipCodes)
      ? searchParams.zipCodes
      : [searchParams.zipCodes];
    zipCodes.forEach((zip) => url.searchParams.append("zipCodes", zip));
  }

  // Handle room filters (convert "any" to undefined)
  if (searchParams.beds && searchParams.beds !== "any") {
    url.searchParams.set(
      "beds",
      Array.isArray(searchParams.beds)
        ? searchParams.beds[0]
        : searchParams.beds
    );
  }
  if (searchParams.baths && searchParams.baths !== "any") {
    url.searchParams.set(
      "baths",
      Array.isArray(searchParams.baths)
        ? searchParams.baths[0]
        : searchParams.baths
    );
  }

  // Handle availability date
  if (searchParams.availableBy) {
    const dateStr = Array.isArray(searchParams.availableBy)
      ? searchParams.availableBy[0]
      : searchParams.availableBy;

    try {
      // Convert to ISO format if it's a date
      const date = new Date(dateStr);
      if (!isNaN(date.getTime())) {
        url.searchParams.set("availableBy", date.toISOString());
      }
    } catch (error) {
      console.warn("Invalid date format for availableBy:", dateStr);
    }
  }

  // Handle pagination and sorting with defaults
  const page = searchParams.page
    ? Array.isArray(searchParams.page)
      ? searchParams.page[0]
      : searchParams.page
    : "0"; // Default to first page

  const size = searchParams.size
    ? Array.isArray(searchParams.size)
      ? searchParams.size[0]
      : searchParams.size
    : "21"; // Default to 21 items per page

  url.searchParams.set("page", page);
  url.searchParams.set("size", size);

  if (searchParams.sortBy) {
    url.searchParams.set(
      "sortBy",
      Array.isArray(searchParams.sortBy)
        ? searchParams.sortBy[0]
        : searchParams.sortBy
    );
  }
  if (searchParams.sortDir) {
    url.searchParams.set(
      "sortDir",
      Array.isArray(searchParams.sortDir)
        ? searchParams.sortDir[0]
        : searchParams.sortDir
    );
  }

  return url.toString();
}
