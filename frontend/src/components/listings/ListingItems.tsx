import { buildApiUrl } from "@/lib/buildApiListingSearchUrl";
import { ListingResponse, PaginatedResponse } from "@/types/ListingResponse";
import Link from "next/link";
import { Suspense } from "react";
import { ListingCard } from "./ListingCard";
import { Pagination } from "./Pagination";

type Props = {
  searchParams: Promise<Record<string, string | string[]>>;
  params?: Promise<{ ListingId: string }>;
};

export function ListingItems(props: Props) {
  return (
    <Suspense fallback={<div>Loading listings...</div>}>
      <ListingContent {...props} />
    </Suspense>
  );
}

export function ListingPagination(props: Props) {
  return (
    <Suspense fallback={null}>
      <PaginationContent {...props} />
    </Suspense>
  );
}

// Function to validate if a listing has valid data
function isValidListing(listing: ListingResponse): boolean {
  // Check for required fields that should not be null/undefined/empty
  if (!listing.zpid || !listing.address || !listing.addressStreet) {
    return false;
  }

  // Check for valid price (should be a string with actual value, not empty or "0")
  if (
    !listing.price ||
    listing.price.trim() === "" ||
    listing.price === "0" ||
    listing.price === "$0"
  ) {
    return false;
  }

  // Check for valid address components
  if (
    !listing.addressCity ||
    !listing.addressState ||
    !listing.addressZipcode
  ) {
    return false;
  }

  // Check for valid image source from Zillow
  if (!listing.imgSrc || listing.imgSrc.trim() === "") {
    return false;
  }

  // Only allow images from photos.zillowstatic.com
  if (!listing.imgSrc.includes("photos.zillowstatic.com")) {
    return false;
  }

  // Check for reasonable property details (optional but if present should be valid)
  if (listing.beds !== null && listing.beds !== undefined && listing.beds < 0) {
    return false;
  }

  if (
    listing.baths !== null &&
    listing.baths !== undefined &&
    listing.baths < 0
  ) {
    return false;
  }

  if (
    listing.area !== null &&
    listing.area !== undefined &&
    listing.area <= 0
  ) {
    return false;
  }

  return true;
}

async function ListingContent({
  searchParams,
}: {
  searchParams: Promise<Record<string, string | string[]>>;
}) {
  const listings = await getListings(searchParams);

  if (!listings) {
    return (
      <div className="text-muted-foreground p-4">Error loading listings</div>
    );
  }

  const listingsArray = Array.isArray(listings) ? listings : listings.content;
  const pagination = Array.isArray(listings) ? null : listings;

  // Filter out listings with invalid/null values
  const validListings = listingsArray.filter(isValidListing);

  // Log filtered count for debugging
  if (listingsArray.length !== validListings.length) {
    console.log(
      `Filtered out ${
        listingsArray.length - validListings.length
      } invalid listings`
    );
  }

  return (
    <div className="space-y-6">
      {validListings.length === 0 ? (
        <div className="text-muted-foreground p-8 text-center">
          No valid listings found matching your criteria
        </div>
      ) : (
        <>
          {/* Results Summary */}
          {pagination && (
            <div className="flex justify-between items-center text-sm text-muted-foreground">
              <span>{pagination.totalElements} total results found</span>
              <span>
                Page {pagination.number + 1} of {pagination.totalPages}
              </span>
            </div>
          )}

          {/* Listings Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {validListings.map((listing) => (
              <Link
                className="block hover:scale-[1.02] transition-transform duration-200"
                key={listing.zpid}
                href={`/listings/${listing.zpid}`}
              >
                <ListingCard listing={listing} />
              </Link>
            ))}
          </div>
        </>
      )}
    </div>
  );
}

async function PaginationContent({
  searchParams,
}: {
  searchParams: Promise<Record<string, string | string[]>>;
}) {
  const listings = await getListings(searchParams);

  if (!listings || Array.isArray(listings)) {
    return null;
  }

  const pagination = listings;

  return (
    <>
      {pagination && pagination.totalPages > 1 && (
        <Pagination pagination={pagination} />
      )}
    </>
  );
}

async function getListings(
  searchParamsPromise: Promise<Record<string, string | string[]>>
): Promise<ListingResponse[] | PaginatedResponse | null> {
  try {
    const searchParams = await searchParamsPromise;
    const apiUrl = buildApiUrl(searchParams);

    console.log("Fetching listings from:", apiUrl);

    const response = await fetch(apiUrl, {
      cache: "no-store",
    });

    if (!response.ok) {
      console.error(
        "Failed to fetch listings:",
        response.status,
        response.statusText
      );
      return null;
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching listings:", error);
    return null;
  }
}
