import { useState } from "react";
import { ZoomIn, ZoomOut, Layers, Navigation } from "lucide-react";

export interface MapMarker {
  id: string;
  x: number;
  y: number;
  type: "deslizamiento" | "inundacion" | "residuos" | "infraestructura" | "servicios" | "alumbrado";
  risk: "low" | "medium" | "high" | "critical";
  label: string;
  neighborhood: string;
}

interface MockMapProps {
  markers?: MapMarker[];
  activeFilters?: string[];
  onMarkerClick?: (marker: MapMarker) => void;
  compact?: boolean;
}

const typeColors: Record<string, string> = {
  deslizamiento:  "#f97316",
  inundacion:     "#3b82f6",
  residuos:       "#a855f7",
  infraestructura:"#f59e0b",
  servicios:      "#06b6d4",
  alumbrado:      "#eab308",
};

const riskColors: Record<string, string> = {
  low:      "#22c55e",
  medium:   "#eab308",
  high:     "#f97316",
  critical: "#ef4444",
};

export const defaultMarkers: MapMarker[] = [
  { id: "1",  x: 390, y: 155, type: "deslizamiento",  risk: "critical", label: "Deslizamiento activo",    neighborhood: "Robledo"      },
  { id: "2",  x: 320, y: 230, type: "inundacion",      risk: "high",     label: "Inundación zona baja",    neighborhood: "Aranjuez"     },
  { id: "3",  x: 450, y: 275, type: "residuos",        risk: "medium",   label: "Vertedero ilegal",        neighborhood: "Manrique"     },
  { id: "4",  x: 360, y: 320, type: "infraestructura", risk: "high",     label: "Vía deteriorada",         neighborhood: "El Centro"    },
  { id: "5",  x: 280, y: 370, type: "deslizamiento",   risk: "high",     label: "Riesgo inestabilidad",    neighborhood: "San Javier"   },
  { id: "6",  x: 430, y: 360, type: "inundacion",      risk: "medium",   label: "Quebrada desbordada",     neighborhood: "Buenos Aires" },
  { id: "7",  x: 370, y: 410, type: "alumbrado",       risk: "low",      label: "Alumbrado dañado",        neighborhood: "Laureles"     },
  { id: "8",  x: 490, y: 420, type: "servicios",       risk: "medium",   label: "Acueducto averiado",      neighborhood: "El Poblado"   },
  { id: "9",  x: 330, y: 460, type: "residuos",        risk: "low",      label: "Punto limpio saturado",   neighborhood: "Belén"        },
  { id: "10", x: 420, y: 195, type: "deslizamiento",   risk: "medium",   label: "Talud inestable",         neighborhood: "Popular"      },
  { id: "11", x: 350, y: 250, type: "inundacion",      risk: "critical", label: "Creciente río Medellín",  neighborhood: "Castilla"     },
  { id: "12", x: 470, y: 310, type: "infraestructura", risk: "low",      label: "Puente en revisión",      neighborhood: "Guayabal"     },
];

export function MockMap({ markers = defaultMarkers, activeFilters, onMarkerClick, compact = false }: MockMapProps) {
  const [zoom, setZoom] = useState(1);
  const [hovered, setHovered] = useState<string | null>(null);

  const visibleMarkers = activeFilters
    ? markers.filter(m => activeFilters.includes(m.type))
    : markers;

  return (
    <div className="relative w-full h-full bg-[#0a1628] rounded-xl overflow-hidden border border-[#1e3a5f]">
      {/* Map base SVG */}
      <svg
        viewBox="0 0 760 560"
        className="w-full h-full"
        style={{ transform: `scale(${zoom})`, transformOrigin: "center", transition: "transform 0.2s" }}
      >
        {/* Background */}
        <rect width="760" height="560" fill="#0a1628" />

        {/* Grid lines */}
        {Array.from({ length: 16 }, (_, i) => (
          <line key={`vg${i}`} x1={i * 50} y1="0" x2={i * 50} y2="560" stroke="#1e3a5f" strokeWidth="0.5" />
        ))}
        {Array.from({ length: 12 }, (_, i) => (
          <line key={`hg${i}`} x1="0" y1={i * 50} x2="760" y2={i * 50} stroke="#1e3a5f" strokeWidth="0.5" />
        ))}

        {/* Mountains west */}
        <polygon points="0,0 180,0 200,80 160,160 140,250 110,320 90,420 60,480 0,560" fill="#0d2137" opacity="0.9" />
        <polygon points="0,0 150,0 175,100 140,200 110,300 80,400 50,500 0,560" fill="#0f2945" opacity="0.7" />
        <polygon points="0,0 120,0 145,120 120,230 95,340 70,450 40,540 0,560" fill="#122d4b" opacity="0.5" />

        {/* Mountains east */}
        <polygon points="760,0 570,0 550,80 590,160 610,250 640,320 660,420 700,480 760,560" fill="#0d2137" opacity="0.9" />
        <polygon points="760,0 600,0 580,100 615,200 645,300 670,400 710,500 760,560" fill="#0f2945" opacity="0.7" />
        <polygon points="760,0 635,0 615,120 645,230 670,340 695,450 725,540 760,560" fill="#122d4b" opacity="0.5" />

        {/* Urban zones */}
        <ellipse cx="385" cy="280" rx="175" ry="240" fill="#112240" opacity="0.9" />
        <ellipse cx="385" cy="280" rx="145" ry="200" fill="#132740" opacity="0.6" />

        {/* District areas */}
        <polygon points="295,130 420,120 445,175 410,190 370,185 300,175" fill="#1a3055" opacity="0.8" />
        <polygon points="240,220 330,200 360,260 330,290 255,285 230,255" fill="#1a3055" opacity="0.7" />
        <polygon points="410,200 490,195 510,255 480,275 420,268 395,240" fill="#1a3055" opacity="0.7" />
        <polygon points="270,300 380,290 390,355 360,375 285,370 255,340" fill="#1a3055" opacity="0.8" />
        <polygon points="390,295 490,285 520,340 495,365 410,360 385,330" fill="#1a3055" opacity="0.7" />
        <polygon points="255,380 380,370 385,435 355,455 265,448 240,415" fill="#1a3055" opacity="0.8" />
        <polygon points="385,370 490,360 510,420 480,445 400,440 380,405" fill="#1a3055" opacity="0.7" />

        {/* Road network */}
        <line x1="385" y1="80"  x2="385" y2="490" stroke="#1e4a7a" strokeWidth="1.5" />
        <line x1="200" y1="270" x2="570" y2="270" stroke="#1e4a7a" strokeWidth="1" />
        <line x1="220" y1="350" x2="545" y2="350" stroke="#1e4a7a" strokeWidth="1" />
        <line x1="340" y1="140" x2="430" y2="450" stroke="#1e4a7a" strokeWidth="0.8" strokeDasharray="4,3" />

        {/* Río Medellín */}
        <path d="M 375,60 C 378,120 370,180 373,240 C 376,300 365,360 368,420 C 371,470 374,510 372,550"
              stroke="#1e6aaa" strokeWidth="5" fill="none" opacity="0.7" />
        <path d="M 375,60 C 378,120 370,180 373,240 C 376,300 365,360 368,420 C 371,470 374,510 372,550"
              stroke="#2d8fd4" strokeWidth="2.5" fill="none" opacity="0.5" />
        <text x="338" y="295" fill="#2d8fd4" fontSize="9" opacity="0.6" transform="rotate(-90 360 280)">Río Medellín</text>

        {/* Metro line */}
        <path d="M 393,70 C 395,130 392,190 391,250 C 390,310 388,370 387,430 C 386,470 386,510 385,550"
              stroke="#f59e0b" strokeWidth="2.5" fill="none" opacity="0.6" strokeDasharray="8,3" />

        {/* Metro stations */}
        {[170, 220, 270, 320, 370, 420, 470].map((y, i) => (
          <circle key={`metro${i}`} cx={391} cy={y} r="4" fill="#0a1628" stroke="#f59e0b" strokeWidth="1.5" opacity="0.8" />
        ))}

        {/* Neighborhood labels */}
        {[
          { x: 348, y: 148, label: "Robledo" },
          { x: 264, y: 248, label: "Aranjuez" },
          { x: 440, y: 228, label: "Manrique" },
          { x: 310, y: 325, label: "El Centro" },
          { x: 260, y: 395, label: "San Javier" },
          { x: 430, y: 330, label: "Buenos Aires" },
          { x: 310, y: 410, label: "Laureles" },
          { x: 435, y: 405, label: "El Poblado" },
          { x: 312, y: 170, label: "Popular" },
        ].map((lbl, i) => (
          <text key={`lbl${i}`} x={lbl.x} y={lbl.y} fill="#4a7aa8" fontSize="8" textAnchor="middle" opacity="0.8">
            {lbl.label}
          </text>
        ))}

        {/* Coordinate labels */}
        {[6.20, 6.22, 6.24, 6.26].map((lat, i) => (
          <text key={`lat${i}`} x="8" y={140 + i * 90} fill="#2a4a6a" fontSize="7" opacity="0.5">{lat}°N</text>
        ))}
        {[-75.62, -75.60, -75.58].map((lng, i) => (
          <text key={`lng${i}`} x={165 + i * 170} y="548" fill="#2a4a6a" fontSize="7" opacity="0.5">{lng}°</text>
        ))}

        {/* Incident markers */}
        {visibleMarkers.map(marker => {
          const isHovered = hovered === marker.id;
          const color = riskColors[marker.risk];
          return (
            <g key={marker.id}
               style={{ cursor: "pointer" }}
               onClick={() => onMarkerClick?.(marker)}
               onMouseEnter={() => setHovered(marker.id)}
               onMouseLeave={() => setHovered(null)}
            >
              {isHovered && (
                <circle cx={marker.x} cy={marker.y} r="18" fill={color} opacity="0.15" />
              )}
              <circle cx={marker.x} cy={marker.y} r="8" fill={color} opacity="0.25" />
              <circle cx={marker.x} cy={marker.y} r="5" fill={color} opacity={isHovered ? 1 : 0.85} />
              <circle cx={marker.x} cy={marker.y} r="2.5" fill="white" opacity="0.9" />
              {marker.risk === "critical" && (
                <circle cx={marker.x} cy={marker.y} r="10" fill="none" stroke={color} strokeWidth="1.5" opacity="0.5">
                  <animate attributeName="r" from="10" to="18" dur="1.5s" repeatCount="indefinite" />
                  <animate attributeName="opacity" from="0.5" to="0" dur="1.5s" repeatCount="indefinite" />
                </circle>
              )}
              {isHovered && (
                <>
                  <rect x={marker.x - 60} y={marker.y - 38} width="120" height="28" rx="4" fill="#0f172a" stroke={color} strokeWidth="1" opacity="0.95" />
                  <text x={marker.x} y={marker.y - 27} fill="#f1f5f9" fontSize="8" textAnchor="middle">{marker.neighborhood}</text>
                  <text x={marker.x} y={marker.y - 17} fill="#94a3b8" fontSize="7" textAnchor="middle">{marker.label}</text>
                </>
              )}
            </g>
          );
        })}

        {/* Scale bar */}
        <line x1="620" y1="535" x2="720" y2="535" stroke="#2a4a6a" strokeWidth="1.5" />
        <line x1="620" y1="530" x2="620" y2="540" stroke="#2a4a6a" strokeWidth="1.5" />
        <line x1="720" y1="530" x2="720" y2="540" stroke="#2a4a6a" strokeWidth="1.5" />
        <text x="670" y="530" fill="#2a4a6a" fontSize="8" textAnchor="middle" opacity="0.7">2 km</text>
      </svg>

      {/* Map controls */}
      {!compact && (
        <div className="absolute bottom-4 right-4 flex flex-col gap-1.5">
          <button
            onClick={() => setZoom(z => Math.min(z + 0.2, 2.5))}
            className="w-8 h-8 bg-[#1e293b] border border-[#334155] rounded-lg flex items-center justify-center text-[#94a3b8] hover:text-white hover:border-[#475569] transition-colors"
          >
            <ZoomIn className="w-4 h-4" />
          </button>
          <button
            onClick={() => setZoom(z => Math.max(z - 0.2, 0.6))}
            className="w-8 h-8 bg-[#1e293b] border border-[#334155] rounded-lg flex items-center justify-center text-[#94a3b8] hover:text-white hover:border-[#475569] transition-colors"
          >
            <ZoomOut className="w-4 h-4" />
          </button>
          <button
            onClick={() => setZoom(1)}
            className="w-8 h-8 bg-[#1e293b] border border-[#334155] rounded-lg flex items-center justify-center text-[#94a3b8] hover:text-white hover:border-[#475569] transition-colors"
          >
            <Navigation className="w-4 h-4" />
          </button>
        </div>
      )}

      {/* Layer indicator */}
      {!compact && (
        <div className="absolute top-4 left-4 bg-[#0f172a]/80 border border-[#334155] rounded-lg px-3 py-1.5 flex items-center gap-2">
          <Layers className="w-3.5 h-3.5 text-blue-400" />
          <span className="text-[#94a3b8] text-xs">Valle de Aburrá — Medellín</span>
        </div>
      )}

      {/* Legend */}
      {!compact && (
        <div className="absolute bottom-4 left-4 bg-[#0f172a]/80 border border-[#334155] rounded-lg p-2.5">
          <div className="text-[#64748b] text-xs mb-2 uppercase tracking-wide">Nivel de riesgo</div>
          {[["#22c55e","Bajo"],["#eab308","Medio"],["#f97316","Alto"],["#ef4444","Crítico"]].map(([color, label]) => (
            <div key={label} className="flex items-center gap-2 mb-1">
              <span className="w-2.5 h-2.5 rounded-full shrink-0" style={{ background: color }} />
              <span className="text-[#94a3b8] text-xs">{label}</span>
            </div>
          ))}
          <div className="mt-1.5 pt-1.5 border-t border-[#334155]">
            <div className="flex items-center gap-2">
              <span className="w-8 h-px bg-yellow-400 opacity-60" style={{ display: "inline-block" }} />
              <span className="text-[#64748b] text-xs">Metro</span>
            </div>
            <div className="flex items-center gap-2 mt-1">
              <span className="w-8 h-px bg-blue-400 opacity-60" style={{ display: "inline-block" }} />
              <span className="text-[#64748b] text-xs">Río</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
