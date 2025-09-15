import Image from "next/image";
import { Card, CardContent } from "../ui/card";
import { Bath, Bed, Calendar, MapPin, Square } from "lucide-react";
import { ListingResponse } from "@/types/ListingResponse";

export function ListingCard({ listing }: { listing: ListingResponse }) {
  const formatPrice = (priceString: string) => {
    const cleanPrice = priceString.replace("/mo", "");
    return cleanPrice;
  };

  // Helper function to safely format numbers
  const safeNumber = (
    value: number | null | undefined,
    fallback: number = 0
  ): number => {
    return value ?? fallback;
  };

  const safeString = (
    value: string | null | undefined,
    fallback: string = "N/A"
  ): string => {
    return value ?? fallback;
  };

  return (
    <Card className="overflow-hidden hover:shadow-lg transition-shadow duration-200 cursor-pointer group p-0">
      {/* Image */}
      <div className="relative w-full h-48 rounded-t-lg overflow-hidden">
        <Image
          src={listing.imgSrc || "/placeholder-house.jpg"}
          alt={`${listing.address} - Property Image`}
          fill
          className="object-cover group-hover:scale-105 transition-transform duration-300"
          sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
        />
      </div>

      {/* Content */}
      <CardContent className="p-4">
        <div className="space-y-4">
          {/* Price - FIXED */}
          <div className="flex items-center justify-between">
            <div className="text-2xl font-semibold text-primary">
              {formatPrice(safeString(listing.price, "$0"))}
              <span className="text-sm text-muted-foreground font-normal">
                /mo
              </span>
            </div>
            {safeNumber(listing.zestimate) > 0 && (
              <div className="text-sm text-muted-foreground">
                Est. ${safeNumber(listing.zestimate).toLocaleString()}
              </div>
            )}
          </div>

          {/* Address */}
          <div className="flex items-start gap-2">
            <MapPin className="w-4 h-4 text-muted-foreground mt-0.5 flex-shrink-0" />
            <div className="text-sm">
              <div className="font-medium">
                {safeString(listing.addressStreet)}
              </div>
              <div className="text-muted-foreground">
                {safeString(listing.addressCity)},{" "}
                {safeString(listing.addressState)}{" "}
                {safeString(listing.addressZipcode)}
              </div>
            </div>
          </div>

          {/* Property Details */}
          <div className="flex items-center gap-4 text-sm text-muted-foreground">
            <div className="flex items-center gap-1">
              <Bed className="w-4 h-4" />
              <span>
                {safeNumber(listing.beds)} bed
                {safeNumber(listing.beds) !== 1 ? "s" : ""}
              </span>
            </div>
            <div className="flex items-center gap-1">
              <Bath className="w-4 h-4" />
              <span>
                {safeNumber(listing.baths)} bath
                {safeNumber(listing.baths) !== 1 ? "s" : ""}
              </span>
            </div>
            {/* Only show area if it exists and is greater than 0 */}
            {safeNumber(listing.area) > 0 && (
              <div className="flex items-center gap-1">
                <Square className="w-4 h-4" />
                <span>{safeNumber(listing.area).toLocaleString()} sqft</span>
              </div>
            )}
          </div>

          {/* Availability Date */}
          {listing.availabilityDate && (
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <Calendar className="w-4 h-4" />
              <span>
                Available{" "}
                {new Date(listing.availabilityDate).toLocaleDateString()}
              </span>
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
