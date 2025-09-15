import { FilterBar } from "@/components/listings/FilterBar";
import {
  ListingItems,
  ListingPagination,
} from "@/components/listings/ListingItems";
import { Suspense } from "react";

interface PageProps {
  searchParams: Promise<Record<string, string | string[]>>;
}

export default async function Home({ searchParams }: PageProps) {
  return (
    <div className="min-h-screen">
      <FilterBar />

      {/* Main Content */}
      <div className="container mx-auto px-6 pt-4 pb-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2">UCF Housing Listings</h1>
          <p className="text-muted-foreground">
            Find your perfect, off-campus home near campus
          </p>
        </div>

        <Suspense fallback={<div>Loading listings...</div>}>
          <ListingItems searchParams={searchParams} />
        </Suspense>
      </div>

      {/* Full Width Pagination - Outside container */}
      <Suspense fallback={null}>
        <ListingPagination searchParams={searchParams} />
      </Suspense>
    </div>
  );
}
