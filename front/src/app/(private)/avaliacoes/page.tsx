"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaStar, FaRegStar, FaReply } from "react-icons/fa";

interface Review {
  id: number;
  clientName?: string;
  barberName?: string;
  rating?: number;
  comment?: string;
  reply?: string;
  createdAt?: string;
}

export default function AvaliacoesPage() {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadReviews();
  }, []);

  async function loadReviews() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/review",
        params: { page: 0, size: 20 },
      });
      const data = response?.data?.content || response?.data || [];
      setReviews(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar avaliações");
    } finally {
      setLoading(false);
    }
  }

  const renderStars = (rating: number = 0) => {
    return Array.from({ length: 5 }, (_, i) =>
      i < rating ? (
        <FaStar key={i} className="text-yellow-400" />
      ) : (
        <FaRegStar key={i} className="text-gray-300" />
      ),
    );
  };

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <h1 className="text-2xl font-bold text-[#1A1A2E]">Avaliações</h1>

        <div className="space-y-4">
          {loading ? (
            Array.from({ length: 3 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse">
                <div className="h-5 bg-gray-200 rounded w-1/3 mb-3" />
                <div className="h-4 bg-gray-200 rounded w-full mb-2" />
                <div className="h-4 bg-gray-200 rounded w-2/3" />
              </div>
            ))
          ) : reviews.length === 0 ? (
            <div className="gobarber-card text-center py-12 text-gray-400">
              Nenhuma avaliação ainda
            </div>
          ) : (
            reviews.map((review) => (
              <div key={review.id} className="gobarber-card">
                <div className="flex items-start justify-between mb-3">
                  <div>
                    <h3 className="font-semibold text-[#1A1A2E]">
                      {review.clientName || "Cliente"}
                    </h3>
                    <p className="text-sm text-gray-500">
                      Barbeiro: {review.barberName || "—"}
                    </p>
                  </div>
                  <div className="flex items-center gap-1">
                    {renderStars(review.rating)}
                  </div>
                </div>
                {review.comment && (
                  <p className="text-gray-600 mb-3">{review.comment}</p>
                )}
                {review.reply && (
                  <div className="bg-gray-50 rounded-lg p-3 mt-2">
                    <p className="text-sm text-gray-500 mb-1 font-medium">
                      Resposta:
                    </p>
                    <p className="text-sm text-gray-600">{review.reply}</p>
                  </div>
                )}
                {!review.reply && (
                  <button className="text-sm text-[#E94560] hover:underline flex items-center gap-1 mt-2">
                    <FaReply /> Responder
                  </button>
                )}
                {review.createdAt && (
                  <p className="text-xs text-gray-400 mt-3">
                    {new Date(review.createdAt).toLocaleDateString("pt-BR")}
                  </p>
                )}
              </div>
            ))
          )}
        </div>
      </div>
    </GoBarberLayout>
  );
}
